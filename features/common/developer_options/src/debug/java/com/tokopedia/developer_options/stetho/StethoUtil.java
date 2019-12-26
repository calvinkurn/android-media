package com.tokopedia.developer_options.remote_config;

import android.content.Context;
import com.facebook.stetho.Stetho;

/**
 * @author okasurya on 2019-08-19.
 */
public class StethoUtil {
    public static void initStetho(Context context) {
        Stetho.initializeWithDefaults(context);
    }
}
