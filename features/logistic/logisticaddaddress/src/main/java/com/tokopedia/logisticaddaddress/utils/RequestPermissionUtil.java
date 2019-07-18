package com.tokopedia.logisticaddaddress.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Fajar Ulin Nuha on 29/10/18.
 */
public class RequestPermissionUtil {

    public static boolean checkHasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
