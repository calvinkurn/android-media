package com.tokopedia.applink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkEntry;
import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.airbnb.deeplinkdispatch.DeepLinkResult;
import com.airbnb.deeplinkdispatch.DeepLinkUri;
import com.airbnb.deeplinkdispatch.Parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author okasurya on 8/30/18.
 * This code is based on airbnb.DeeplinkDispatch's generated code (DeeplinkDelegate class)
 */
public class TkpdApplinkDelegate implements ApplinkDelegate {
    private static final String TAG = TkpdApplinkDelegate.class.getSimpleName();

    private final List<? extends Parser> loaders;

    public TkpdApplinkDelegate(Parser... parsers) {
        this.loaders = Arrays.asList(parsers);
    }

    private static DeepLinkResult createResultAndNotify(Context context, final boolean successful, final Uri uri, final String error) {
        notifyListener(context, !successful, uri, error);
        return new DeepLinkResult(successful, uri != null ? uri.toString() : null, error);
    }

    private static void notifyListener(Context context, boolean isError, Uri uri, String errorMessage) {
        Intent intent = new Intent();
        intent.setAction(DeepLinkHandler.ACTION);
        intent.putExtra(DeepLinkHandler.EXTRA_URI, uri != null ? uri.toString() : "");
        intent.putExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, !isError);
        if (isError) {
            intent.putExtra(DeepLinkHandler.EXTRA_ERROR_MESSAGE, errorMessage);
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private DeepLinkEntry findEntry(String uriString) {
        for (Parser loader : loaders) {
            DeepLinkEntry entry = loader.parseUri(uriString);
            if (entry != null) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public DeepLinkResult dispatchFrom(Activity activity, Intent sourceIntent) {
        if (activity == null) {
            throw new NullPointerException("activity == null");
        }
        if (sourceIntent == null) {
            throw new NullPointerException("sourceIntent == null");
        }
        Uri uri = sourceIntent.getData();
        if (uri == null) {
            return createResultAndNotify(activity, false, null, "No Uri in given activity's intent.");
        }
        AppUtil.logAirBnbUsage(uri);
        String uriString = uri.toString();
        DeepLinkEntry entry = findEntry(uriString);
        if (entry != null) {
            DeepLinkUri deepLinkUri = DeepLinkUri.parse(uriString);
            Map<String, String> parameterMap = entry.getParameters(uriString);
            for (String queryParameter : deepLinkUri.queryParameterNames()) {
                for (String queryParameterValue : deepLinkUri.queryParameterValues(queryParameter)) {
                    if (parameterMap.containsKey(queryParameter)) {
                        Log.w(TAG, "Duplicate parameter name in path and query param: " + queryParameter);
                    }
                    parameterMap.put(queryParameter, queryParameterValue);
                }
            }
            parameterMap.put(DeepLink.URI, uri.toString());
            Bundle parameters;
            if (sourceIntent.getExtras() != null) {
                parameters = new Bundle(sourceIntent.getExtras());
            } else {
                parameters = new Bundle();
            }
            for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet()) {
                parameters.putString(parameterEntry.getKey(), parameterEntry.getValue());
            }
            try {
                Class<?> c = entry.getActivityClass();
                Intent newIntent;
                TaskStackBuilder taskStackBuilder = null;
                if (entry.getType() == DeepLinkEntry.Type.CLASS) {
                    newIntent = new Intent(activity, c);
                } else {
                    Method method;
                    try {
                        method = c.getMethod(entry.getMethod(), Context.class);
                        if (method.getReturnType().equals(TaskStackBuilder.class)) {
                            taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity);
                            if (taskStackBuilder.getIntentCount() == 0) {
                                return createResultAndNotify(activity, false, uri, "Could not deep link to method: " + entry.getMethod() + " intents length == 0");
                            }
                            newIntent = taskStackBuilder.editIntentAt(taskStackBuilder.getIntentCount() - 1);
                        } else {
                            newIntent = (Intent) method.invoke(c, activity);
                        }
                    } catch (NoSuchMethodException exception) {
                        method = c.getMethod(entry.getMethod(), Context.class, Bundle.class);
                        if (method.getReturnType().equals(TaskStackBuilder.class)) {
                            taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity, parameters);
                            if (taskStackBuilder.getIntentCount() == 0) {
                                return createResultAndNotify(activity, false, uri, "Could not deep link to method: " + entry.getMethod() + " intents length == 0");
                            }
                            newIntent = taskStackBuilder.editIntentAt(taskStackBuilder.getIntentCount() - 1);
                        } else {
                            newIntent = (Intent) method.invoke(c, activity, parameters);
                        }
                    }
                }
                if (newIntent.getAction() == null) {
                    newIntent.setAction(sourceIntent.getAction());
                }
                if (newIntent.getData() == null) {
                    newIntent.setData(sourceIntent.getData());
                }
                removeDuplicateKeys(newIntent,parameters);
                newIntent.putExtras(parameters);
                newIntent.putExtra(DeepLink.IS_DEEP_LINK, true);
                newIntent.putExtra(DeepLink.REFERRER_URI, uri);
                if (activity.getCallingActivity() != null) {
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                }
                if (taskStackBuilder != null) {
                    taskStackBuilder.startActivities();
                } else {
                    activity.startActivity(newIntent);
                }
                return createResultAndNotify(activity, true, uri, null);
            } catch (NoSuchMethodException exception) {
                return createResultAndNotify(activity, false, uri, "Deep link to non-existent method: " + entry.getMethod());
            } catch (IllegalAccessException exception) {
                return createResultAndNotify(activity, false, uri, "Could not deep link to method: " + entry.getMethod());
            } catch (InvocationTargetException exception) {
                return createResultAndNotify(activity, false, uri, "Could not deep link to method: " + entry.getMethod());
            }
        } else {
            return createResultAndNotify(activity, false, uri, "No registered entity to handle deep link: " + uri.toString());
        }
    }
    private void removeDuplicateKeys(Intent newIntent,Bundle parameters){
        for (Iterator<String> iterator = parameters.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (newIntent.hasExtra(key)) {
                iterator.remove();
            }
        }
    }

    /**
     * Search and return an Intent from applink url
     * if url is not valid / intent is not found, then it will throw an exception
     *
     * To make sure that the intent is available,
     * please call {@link TkpdApplinkDelegate#supportsUri(String)} first
     *
     * @param activity
     * @param applink
     * @return
     * @throws Exception
     */
    @Override
    public Intent getIntent(Activity activity, String applink) throws Exception {
        if (activity == null) {
            throw new NullPointerException("activity == null");
        }

        Uri uri = Uri.parse(applink);
        if (uri == null) {
            throw new Exception("No Uri in given activity's intent.");
        }
        AppUtil.logAirBnbUsage(uri);
        String uriString = uri.toString();
        DeepLinkEntry entry = findEntry(uriString);
        if (entry != null) {
            DeepLinkUri deepLinkUri = DeepLinkUri.parse(uriString);
            Map<String, String> parameterMap = entry.getParameters(uriString);
            for (String queryParameter : deepLinkUri.queryParameterNames()) {
                for (String queryParameterValue : deepLinkUri.queryParameterValues(queryParameter)) {
                    if (parameterMap.containsKey(queryParameter)) {
                        Log.w(TAG, "Duplicate parameter name in path and query param: " + queryParameter);
                    }
                    parameterMap.put(queryParameter, queryParameterValue);
                }
            }
            parameterMap.put(DeepLink.URI, uri.toString());
            Bundle parameters = new Bundle();

            for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet()) {
                parameters.putString(parameterEntry.getKey(), parameterEntry.getValue());
            }

            Class<?> c = entry.getActivityClass();
            Intent newIntent;
            if (entry.getType() == DeepLinkEntry.Type.CLASS) {
                newIntent = new Intent(activity, c);
            } else {
                Method method;
                try {
                    method = c.getMethod(entry.getMethod(), Context.class);
                    if (method.getReturnType().equals(TaskStackBuilder.class)) {
                        throw new Exception("Instead of intent, applink produce a TaskStackBuilder instance");
                    } else {
                        newIntent = (Intent) method.invoke(c, activity);
                    }
                } catch (NoSuchMethodException exception) {
                    method = c.getMethod(entry.getMethod(), Context.class, Bundle.class);
                    if (method.getReturnType().equals(TaskStackBuilder.class)) {
                        throw new Exception("Instead of intent, applink produce a TaskStackBuilder instance");
                    } else {
                        newIntent = (Intent) method.invoke(c, activity, parameters);
                    }
                }
            }

            if (newIntent.getData() == null) {
                newIntent.setData(uri);
            }
            newIntent.putExtras(parameters);
            newIntent.putExtra(DeepLink.IS_DEEP_LINK, true);
            newIntent.putExtra(DeepLink.REFERRER_URI, uri);

            return newIntent;
        } else {
            throw new Exception("null entry");
        }
    }

    /**
     * Search and return an TaskStackBuilder based on applink url
     * If url is not valid or TaskStackBuilder is not found, it will throw an exception
     *
     * To make sure that the TaskStackBuilder is available,
     * please call {@link TkpdApplinkDelegate#supportsUri(String)} first
     *
     * @param activity
     * @param applink
     * @return
     * @throws Exception
     */
    @Override
    public TaskStackBuilder getTaskStackBuilder(Activity activity, String applink) throws Exception {
        if (activity == null) {
            throw new NullPointerException("activity == null");
        }

        Uri uri = Uri.parse(applink);
        if (uri == null) {
            throw new Exception("No Uri in given activity's intent.");
        }
        AppUtil.logAirBnbUsage(uri);
        String uriString = uri.toString();
        DeepLinkEntry entry = findEntry(uriString);
        if (entry != null) {
            DeepLinkUri deepLinkUri = DeepLinkUri.parse(uriString);
            Map<String, String> parameterMap = entry.getParameters(uriString);
            for (String queryParameter : deepLinkUri.queryParameterNames()) {
                for (String queryParameterValue : deepLinkUri.queryParameterValues(queryParameter)) {
                    if (parameterMap.containsKey(queryParameter)) {
                        Log.w(TAG, "Duplicate parameter name in path and query param: " + queryParameter);
                    }
                    parameterMap.put(queryParameter, queryParameterValue);
                }
            }
            parameterMap.put(DeepLink.URI, uri.toString());
            Bundle parameters = new Bundle();

            for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet()) {
                parameters.putString(parameterEntry.getKey(), parameterEntry.getValue());
            }

            Class<?> c = entry.getActivityClass();
            TaskStackBuilder taskStackBuilder;
            if (entry.getType() != DeepLinkEntry.Type.CLASS) {
                Method method;
                try {
                    method = c.getMethod(entry.getMethod(), Context.class);
                    if (method.getReturnType().equals(TaskStackBuilder.class)) {
                        taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity);
                        if (taskStackBuilder.getIntentCount() == 0) {
                            throw new Exception("Could not deep link to method: " + entry.getMethod() + " intents length == 0");
                        }
                    } else {
                        throw new Exception("Instead of TaskStackBuilder, applink produce an Intent instance");
                    }
                } catch (NoSuchMethodException exception) {
                    method = c.getMethod(entry.getMethod(), Context.class, Bundle.class);
                    if (method.getReturnType().equals(TaskStackBuilder.class)) {
                        taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity, parameters);
                        if (taskStackBuilder.getIntentCount() == 0) {
                            return null;
                        }
                    } else {
                        throw new Exception("Instead of TaskStackBuilder, applink produce an Intent instance");
                    }
                }
            } else {
                throw new Exception("Instead of TaskStackBuilder, applink produce an Intent instance");
            }

            return taskStackBuilder;
        } else {
            throw new Exception("No registered entity to handle deep link: " + uri.toString());
        }
    }

    @Override
    public boolean supportsUri(String uriString) {
        return findEntry(uriString) != null;
    }
}
