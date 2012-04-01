package org.selenium.androframework.keywords;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.selenium.androframework.common.Command;

public abstract class BaseKeywordDefinitions {
	protected HashMap<String, Integer> methodMap = new HashMap<String, Integer>();

	abstract public boolean methodSUpported(Command command);

	protected void collectSupportedMethods(Class c) {
		Iterator<Method> it = Arrays.asList(c.getMethods()).iterator();
		while (it.hasNext()) {
			Method method = it.next();
			if (isSupported(method)) {
				Integer noOfParam = new Integer(
						method.getParameterTypes().length);
				methodMap.put(method.getName(), noOfParam);
			}
		}
		System.out.println(methodMap);
	}

	private boolean isSupported(Method method) {
		Class<?>[] params = method.getParameterTypes();
		int paramLength = params.length;
		if (paramLength == 0)
			return true;
		if (paramLength == 1
				&& (params[0].isAssignableFrom(String.class) || params[0]
						.isAssignableFrom(int.class)))
			return true;
		if (paramLength == 2
				&& (params[0].isAssignableFrom(String.class) && params[1]
						.isAssignableFrom(String.class)))
			return true;

		return false;
	}

	protected void invoker(Object obj,
			String methodName, List<String> s) {
		if (s.size() == 0) {
			try {
				Method method = obj.getClass().getMethod(methodName,
						(Class<?>) null);
				method.invoke(obj, (Class<?>) null);
			} catch (NoSuchMethodException e) {
				System.out.println("nosuch method exception thrown: " + e);
			} catch (Exception e) {
				System.out.println("exception thrown: " + e);
			}
		}
		else if (s.size() == 1) {
			int tempVal = 0;
			boolean isInteger = false;
			try {
				tempVal = Integer.parseInt(s.get(0));
				isInteger = true;
			} catch (NumberFormatException e) {

			}
			if (isInteger) {
				try {
					Method method = obj.getClass().getMethod(methodName,
							int.class);
					method.invoke(obj, tempVal);
				} catch (NoSuchMethodException e) {
					System.out.println("nosuch method exception thrown: " + e);
				} catch (Exception e) {
					System.out.println("exception thrown: " + e);
				}
			} else {
				try {
					Method method = obj.getClass().getMethod(methodName,
							String.class);
					method.invoke(obj, s.get(0));
				} catch (NoSuchMethodException e) {
					Assert.fail("nosuch method exception thrown: " + e);
				} catch (InvocationTargetException e) {
					Assert.fail("exception thrown: " + e);
				} catch (IllegalArgumentException e) {
					Assert.fail(e.toString());
				} catch (IllegalAccessException e) {
					Assert.fail(e.toString());
				}
			}
		}
		else if (s.size() > 1) {
			try {
				Method method = obj.getClass().getMethod(methodName,
						String.class, String.class);
				method.invoke(obj, s.get(0), s.get(1));
			} catch (NoSuchMethodException e) {
				System.out.println("nosuch method exception thrown: " + e);
			} catch (Exception e) {
				System.out.println("exception thrown: " + e);
			}
		}

	}

	abstract public void execute(Command command);

}
