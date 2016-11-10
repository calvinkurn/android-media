package com.tokopedia.core.util;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Nathaniel on 11/9/2016.
 */

public class RouterUtils {

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
}