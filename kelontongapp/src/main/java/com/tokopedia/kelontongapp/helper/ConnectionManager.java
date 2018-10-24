package com.tokopedia.kelontongapp.helper;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;

/**
 * Created by meta on 23/10/18.
 */
public class ConnectionManager {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null)
            return cm.getActiveNetworkInfo() != null;
        return false;
    }
}
