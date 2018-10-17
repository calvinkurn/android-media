package com.tkpd.library.utils;

import android.content.Context;
import android.os.Build;

import com.logentries.logger.AndroidLogger;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
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
    private static final String TOKEN_LOG_NOTIFIER_NOTP = "44ec54a0-bcc2-437e-a061-9c7b3e124165";

    public static void logForceLogout(Context context, GCMHandler gcmHandler, SessionHandler sessionHandler, String url) {
        String baseUrl = getBaseUrl(url);

        AnalyticsLog.log(context, "ErrorType=Force Logout!"
                + " UserID=" + (sessionHandler.getLoginID()
                .equals("") ? "0" : sessionHandler.getLoginID())
                + " Url=" + "'" + url + "'"
                + " BaseUrl=" + "'" + baseUrl + "'"
                + " AppPackage=" + GlobalConfig.getPackageApplicationName()
                + " AppVersion=" + GlobalConfig.VERSION_NAME
                + " AppCode=" + GlobalConfig.VERSION_CODE
                + " OSVersion=" + Build.VERSION.RELEASE
                + " DeviceModel=" + Build.MODEL
                + " DeviceId=" + "'" + gcmHandler.getRegistrationId() + "'"
                + " Environment=" + isStaging(baseUrl)

        );
    }

    public static void logForceLogoutToken(GCMHandler gcmHandler, SessionHandler sessionHandler, String url) {
        String baseUrl = getBaseUrl(url);

        AnalyticsLog.log(context,"ErrorType=Force Logout Token!"
                + " UserID=" + (sessionHandler.getLoginID()
                .equals("") ? "0" : sessionHandler.getLoginID())
                + " Url=" + "'" + url + "'"
                + " BaseUrl=" + "'" + baseUrl + "'"
                + " AppPackage=" + GlobalConfig.getPackageApplicationName()
                + " AppVersion=" + GlobalConfig.VERSION_NAME
                + " AppCode=" + GlobalConfig.VERSION_CODE
                + " OSVersion=" + Build.VERSION.RELEASE
                + " DeviceModel=" + Build.MODEL
                + " DeviceId=" + "'" + gcmHandler.getRegistrationId() + "'"
                + " Environment=" + isStaging(baseUrl)

        );
    }


    public static void logNetworkError(GCMHandler gcmHandler, SessionHandler sessionHandler, String url, int errorCode) {
        String baseUrl = getBaseUrl(url);

        AnalyticsLog.log("ErrorType=Error Network! "
                + " ErrorCode=" + errorCode
                + " UserID=" + (sessionHandler.getLoginID()
                .equals("") ? "0" : sessionHandler.getLoginID())
                + " Url=" + "'" + url + "'"
                + " BaseUrl=" + "'" + baseUrl + "'"
                + " AppPackage=" + GlobalConfig.getPackageApplicationName()
                + " AppVersion=" + GlobalConfig.VERSION_NAME
                + " AppCode=" + GlobalConfig.VERSION_CODE
                + " OSVersion=" + Build.VERSION.RELEASE
                + " DeviceModel=" + Build.MODEL
                + " DeviceId=" + "'" + gcmHandler.getRegistrationId() + "'"
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

    public static void logNotification(SessionHandler sessionHandler, String notificationId, String notificationCode) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(MainApplication.getAppContext());
        if (remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.NOTIFICATION_LOGGER, false)) {
            AnalyticsLog.log("Notification Received. User: " + sessionHandler.getLoginID()
                    + " Notification Id: " + notificationId
                    + " Notification Code: " + notificationCode
            );
        }
    }

    private static AndroidLogger mInstance = null;

    private static AndroidLogger getAndroidNOTPLogger(Context context) {
        try {
            if (mInstance == null) {
                mInstance = AndroidLogger.createInstance(
                        context,
                        false,
                        true,
                        false,
                        null,
                        0,
                        TOKEN_LOG_NOTIFIER_NOTP,
                        true);
            }
            return mInstance;
        } catch (IOException e) {
            return null;
        }
    }

    public static void printNOTPLog(Context context, String msg) {
        getAndroidNOTPLogger(context).log(msg + " - Phone Number:-" + new SessionHandler(context).getPhoneNumber()
                + " - LoginID - " + new SessionHandler(context).getLoginID(MainApplication.getAppContext()));
    }

    private static void log(Context context, String message) {
        try {
            AndroidLogger logger = getAndroidLogger(context);
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
    private static AndroidLogger getAndroidLogger(Context context) {
        if (instance == null) {
            try {
                instance = AndroidLogger.createInstance(
                        context,
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

    public static void logInvalidGrant(String url) {
        String baseUrl = getBaseUrl(url);

        AnalyticsLog.log("ErrorType=Invalid Grant!"
                + " UserID=" + (SessionHandler.getLoginID(MainApplication.getAppContext())
                .equals("") ? "0" : SessionHandler.getLoginID(MainApplication.getAppContext()))
                + " Url=" + "'" + url + "'"
                + " BaseUrl=" + "'" + baseUrl + "'"
                + " AppPackage=" + GlobalConfig.getPackageApplicationName()
                + " AppVersion=" + GlobalConfig.VERSION_NAME
                + " AppCode=" + GlobalConfig.VERSION_CODE
                + " OSVersion=" + Build.VERSION.RELEASE
                + " DeviceModel=" + Build.MODEL
                + " DeviceId=" + "'" + GCMHandler.getRegistrationId(MainApplication.getAppContext()) + "'"
                + " Environment=" + isStaging(baseUrl)
                + " RefreshToken=" + "'" + (SessionHandler.getRefreshToken(MainApplication.getAppContext())) + "'"
        );
    }
}
