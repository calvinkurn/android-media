package com.tokopedia.core.util;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;

/**
 * Created by Nathaniel on 11/9/2016.
 */

public class RouterUtils {

    public static TkpdCoreRouter getRouterFromContext (Context context) {
        Application application = null;
        if (context instanceof Activity) {
            application = ((Activity) context).getApplication();
        } else if (context instanceof Service) {
            application = ((Service) context).getApplication();
        } else if (context instanceof Application) {
            application = (Application) context;
        }
        if (application == null) {
            return getDefaultRouter();
        } else {
            return (TkpdCoreRouter) application;
        }
    }

    public static TkpdCoreRouter getDefaultRouter() {
        return (TkpdCoreRouter) MainApplication.getInstance();
    }

    public static Intent getActivityIntent(Context context, String activityFullPath) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), activityFullPath);
        return intent;
    }

    public static Fragment getFragment(Context context, String className) {
        return Fragment.instantiate(context, className);
    }

    public static ComponentName getActivityComponentName(Context context, String activityFullPath) {
        ComponentName componentName = new ComponentName(context.getPackageName(), activityFullPath);
        return componentName;
    }

    public static ComponentName getActivityComponentName(Context context, Class<?> clss){
        return new ComponentName(context, clss);
    }

    public static Class<?> getActivityClass(String activityFullPath) throws ClassNotFoundException {
        return Class.forName(activityFullPath);
    }
}