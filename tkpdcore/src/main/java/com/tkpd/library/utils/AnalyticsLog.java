package com.tkpd.library.utils;

import android.content.Context;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.logentries.logger.AndroidLogger;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import java.io.IOException;

/**
 * Created by ricoharisin on 8/23/16.
 */
public class AnalyticsLog {
    private static AndroidLogger instance;

    public static void logForceLogout(String url) {
        AnalyticsLog.log("Force Logout! User: " + SessionHandler.getLoginID(MainApplication.getAppContext())
                + " Device ID: " + GCMHandler.getRegistrationId(MainApplication.getAppContext())
                + " Last Access Url: " + url);
    }

    public static void logNetworkError(String url, int errorCode) {
        AnalyticsLog.log("Error Network! User: " + SessionHandler.getLoginID(MainApplication.getAppContext())
                + " URL: " + url
                + " Error Code: " + errorCode);
    }

    public static void logNotification(String notificationId, String notificationCode) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(MainApplication.getAppContext());
        if (remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.NOTIFICATION_LOGGER, false)) {
            AnalyticsLog.log("Notification Received. User: " + SessionHandler.getLoginID(MainApplication.getAppContext())
                    + " Notification Id: " + notificationId
                    + " Notification Code: " + notificationCode
            );
        }
    }

    private static void log(String message) {
        AndroidLogger logger = getAndroidLogger();
        if (logger != null) {
            logger.log(message);
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
