package com.tkpd.library.utils;

import android.os.Build;

import com.logentries.logger.AndroidLogger;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ricoharisin on 8/23/16.
 */
public class AnalyticsLog {
    private static AndroidLogger instance;
    private static final String TOKEN_LOG_NOTIFIER = "2719adf1-18c8-4cc6-8c92-88a07594f7db";

    public static void logForceLogout(String url) {
        String baseUrl = getBaseUrl(url);

        AnalyticsLog.log("ErrorType=Force Logout!"
                + " UserID=" + (SessionHandler.getLoginID(MainApplication.getAppContext())
                .equals("") ? "0" : SessionHandler.getLoginID(MainApplication.getAppContext()))
                + " Url=" + "'" + url + "'"
                + " BaseUrl=" + "'" + baseUrl + "'"
                + " AppPackage=" + GlobalConfig.getPackageApplicationName()
                + " AppVersion=" + GlobalConfig.VERSION_NAME
                + " AppCode=" + GlobalConfig.VERSION_CODE
                + " OSVersion=" + Build.VERSION.RELEASE
                + " DeviceModel=" + android.os.Build.MODEL
                + " DeviceId=" + "'" + GCMHandler.getRegistrationId(MainApplication.getAppContext()) + "'"
                + " Environment=" + isStaging(baseUrl)

        );
    }

    public static void logNetworkError(String url, int errorCode) {
        String baseUrl = getBaseUrl(url);

        AnalyticsLog.log("ErrorType=Error Network! "
                + " ErrorCode=" + errorCode
                + " UserID=" + (SessionHandler.getLoginID(MainApplication.getAppContext())
                .equals("") ? "0" : SessionHandler.getLoginID(MainApplication.getAppContext()))
                + " Url=" + "'" + url + "'"
                + " BaseUrl=" + "'" + baseUrl + "'"
                + " AppPackage=" + GlobalConfig.getPackageApplicationName()
                + " AppVersion=" + GlobalConfig.VERSION_NAME
                + " AppCode=" + GlobalConfig.VERSION_CODE
                + " OSVersion=" + Build.VERSION.RELEASE
                + " DeviceModel=" + android.os.Build.MODEL
                + " DeviceId=" + "'" + GCMHandler.getRegistrationId(MainApplication.getAppContext()) + "'"
                + " Environment=" + isStaging(baseUrl)


        );
    }


    private static String getBaseUrl(String url) {
        try {
            URL stringUrl = new URL(url);
            return stringUrl.getProtocol() + "://" + stringUrl.getHost() + stringUrl
                    .getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return url;
        }
    }

    private static String isStaging(String baseUrl) {
        return baseUrl.contains("staging") ? "Staging" : "Production";
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
        try {
            AndroidLogger logger = getAndroidLogger();
            if (logger != null) {
                logger.log(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
