package com.tokopedia.developer_options.stetho;

import android.content.Context;
import com.facebook.stetho.Stetho;

/**
 * @author Vishal Gupta on 2019-12-30.
 */
public class StethoUtil {
    public static void initStetho(Context context) {
        Stetho.initializeWithDefaults(context);
    }
}
