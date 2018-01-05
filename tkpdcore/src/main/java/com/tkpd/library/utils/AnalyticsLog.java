package com.tkpd.library.utils;

import android.content.Context;

import com.logentries.logger.AndroidLogger;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;

import java.io.IOException;

/**
 * Created by ricoharisin on 8/23/16.
 */
public class AnalyticsLog {
    private static AndroidLogger instance;

    public static void logForceLogout(String url) {
        AndroidLogger logger = getAndroidLogger();
        if (logger != null) {
            logger.log("Force Logout! User: " + SessionHandler.getLoginID(MainApplication.getAppContext())
                    + " Device ID: " + GCMHandler.getRegistrationId(MainApplication.getAppContext())
                    + " Last Access Url: " + url);
        }
    }

    public static void logNetworkError(String url, int errorCode) {
        AndroidLogger logger = getAndroidLogger();
        if (logger != null) {
            logger.log("Error Network! User: " + SessionHandler.getLoginID(MainApplication.getAppContext())
                    + " URL: " + url
                    + " Error Code: " + errorCode);
        }
    }

    /**
     * Get instance of AndroidLogger.
     * It is a singleton because LogEntries will throw IllegalStateException
     * if multiple instance created at the same time.
     *
     * @return single instance of AndroidLogger
     */
    private static AndroidLogger getAndroidLogger() {
        if (instance == null) {
            try {
                instance = AndroidLogger.createInstance(
                        MainApplication.getAppContext(),
                        false,
                        false,
                        false,
                        null,
                        0,
                        "2719adf1-18c8-4cc6-8c92-88a07594f7db",
                        false
                );
            } catch (IOException ignore) {
            }
        }

        return instance;
    }
}
