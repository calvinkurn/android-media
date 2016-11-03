package com.tkpd.library.utils;


import android.util.Log;

/**
 * Created by ricoharisin on 2/2/15.
 */
public class Logger {

    public static void i (String TAG, String msg) {
        System.out.println("STRING SIZE: "+msg.length());
        dump(TAG, msg);
    }

    public static void dump (String TAG, String msg) {
        if (msg.length() > 4000 ) {
            int end = 4001;
            Log.i(TAG, msg.substring(0, 4000));
            dump (TAG, msg.substring(end));
        } else {
            Log.i(TAG, msg);
        }
    }

}
