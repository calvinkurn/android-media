package com.tkpd.library.utils;

import android.os.Build;

import com.logentries.logger.AndroidLogger;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import java.io.IOException;

/**
 * Created by ricoharisin on 8/23/16.
 */
public class AnalyticsLog {
    private static AndroidLogger instance;
    private static final String TOKEN_LOG_NOTIFIER = "2719adf1-18c8-4cc6-8c92-88a07594f7db";

    public static void logForceLogout(String url) {
        AnalyticsLog.log("Force Logout! User: " + SessionHandler.getLoginID(MainApplication.getAppContext())
                + " Last Access Url: " + url
                + " App Version : " + GlobalConfig.getPackageApplicationName() + " " + GlobalConfig.VERSION_NAME
                + " App Code : " + GlobalConfig.VERSION_CODE
                + " Android Version : " + Build.VERSION.RELEASE
                + " Android Model : " + android.os.Build.MODEL
                + " Device ID: " + GCMHandler.getRegistrationId(MainApplication.getAppContext())

        );
    }

    public static void logNetworkError(String url, int errorCode) {
        AnalyticsLog.log("Error Network! User: " + SessionHandler.getLoginID(MainApplication.getAppContext())
                + " URL: " + url
                + " Error Code: " + errorCode
                + " App Version : " + GlobalConfig.getPackageApplicationName() + " " + GlobalConfig.VERSION_NAME
                + " App Code : " + GlobalConfig.VERSION_CODE
                + " Android Version : " + Build.VERSION.RELEASE
                + " Android Model : " + android.os.Build.MODEL
                + " Device ID: " + GCMHandler.getRegistrationId(MainApplication.getAppContext())
        );
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
                        TOKEN_LOG_NOTIFIER,
                        false
                );
            } catch (IOException ignore) {
            }
        }

        return instance;
    }
}
