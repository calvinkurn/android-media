package com.tokopedia.abstraction.common.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;


/**
 * Created by Nisie on 8/5/16.
 */
public class RequestPermissionUtil {

    public static boolean checkHasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }


}
