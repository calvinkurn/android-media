package com.tkpd.library.utils;

import android.content.Context;

import com.logentries.logger.AndroidLogger;
import com.tokopedia.tkpd.app.MainApplication;
import com.tokopedia.tkpd.gcm.GCMHandler;
import com.tokopedia.tkpd.util.NewTokenHandler;
import com.tokopedia.tkpd.util.SessionHandler;

import java.io.IOException;

/**
 * Created by ricoharisin on 8/23/16.
 */
public class AnalyticsLog {

    public static void logForceLogout(String url) {
        Context context = MainApplication.getAppContext();
        AndroidLogger logger = getAndroidLogger(context);
        if (logger != null)  {
            logger.log("Force Logout! User: "+ SessionHandler.getLoginID(context)
                        +" Device ID: "+ GCMHandler.getRegistrationId(context)
                        +" Last Access Url: "+url);
        }
    }

    public static void logNetworkError(String url, int errorCode) {
        Context context = MainApplication.getAppContext();
        AndroidLogger logger = getAndroidLogger(context);
        if (logger != null)  {
            logger.log("Error Network! User: "+ SessionHandler.getLoginID(context)
                    +" URL: "+ url
                    +" Error Code: "+errorCode);
        }
    }

    public static AndroidLogger getAndroidLogger(Context context) {
        try {
            return AndroidLogger.createInstance(
                    MainApplication.getAppContext(),
                    false,
                    false,
                    false,
                    null,
                    0,
                    "2719adf1-18c8-4cc6-8c92-88a07594f7db",
                    false
            );
        } catch (IOException e) {
           return null;
        }
    }
}
