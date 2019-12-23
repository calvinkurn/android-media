package com.tokopedia.tkpd.utils;

import android.content.Context;
import android.provider.Settings;
import com.facebook.stetho.Stetho;

/**
 * @author okasurya on 2019-08-19.
 */
public class StethoUtil {
    public static void initStetho(Context context) {
        Stetho.initializeWithDefaults(context);
        //return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
