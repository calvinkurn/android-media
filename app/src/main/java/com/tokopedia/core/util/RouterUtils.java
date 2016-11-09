package com.tokopedia.core.util;

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

}