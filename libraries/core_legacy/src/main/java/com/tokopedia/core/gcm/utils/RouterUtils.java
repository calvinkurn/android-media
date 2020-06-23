package com.tokopedia.core.gcm.utils;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.TkpdCoreRouter;


/**
 * Created by Nathaniel on 11/9/2016.
 */

public class RouterUtils {

    public static TkpdCoreRouter getRouterFromContext(Context context) {
        return (TkpdCoreRouter) context.getApplicationContext();
    }

    public static Intent getActivityIntent(Context context, String activityFullPath) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), activityFullPath);
        return intent;
    }

    public static Fragment getFragment(Context context, String className) {
        return Fragment.instantiate(context, className);
    }

    public static Class<?> getActivityClass(String activityFullPath) throws ClassNotFoundException {
        return Class.forName(activityFullPath);
    }
}