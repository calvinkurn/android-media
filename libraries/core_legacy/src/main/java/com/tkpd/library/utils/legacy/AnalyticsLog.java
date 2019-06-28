package com.tkpd.library.utils.legacy;

import android.content.Context;
import android.os.Build;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.track.TrackApp;

import java.net.MalformedURLException;
import java.net.URL;

import timber.log.Timber;

/**
 * Created by ricoharisin on 8/23/16.
 */
public class AnalyticsLog {
    private static final String EVENT_CLICK_LOGOUT = "clickLogout";
    private static final String CATEGORY_FORCE_LOGOUT = "force logout";

    public static void logForceLogout(Context context, GCMHandler gcmHandler, SessionHandler sessionHandler, String url, boolean isInvalidToken, boolean isRequestDenied) {
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

        if (isInvalidToken) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(
                    EVENT_CLICK_LOGOUT,
                    CATEGORY_FORCE_LOGOUT,
                    baseUrl,
                    "get invalid request"
            );
        } else if (isRequestDenied) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(
                    EVENT_CLICK_LOGOUT,
                    CATEGORY_FORCE_LOGOUT,
                    baseUrl,
                    "request denied"
            );
        }
    }

    public static void logForceLogoutToken(Context context, GCMHandler gcmHandler, SessionHandler sessionHandler, String url) {
        String baseUrl = getBaseUrl(url);

        AnalyticsLog.log(context, "ErrorType=Force Logout Token!"
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

        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_LOGOUT,
                CATEGORY_FORCE_LOGOUT,
                baseUrl,
                "get invalid token"
        );
    }


    public static void logGroupChatWebSocketError(Context context, String url, String error) {
        TkpdCoreRouter coreRouter = RouterUtils.getRouterFromContext(context);
        SessionHandler sessionHandler = coreRouter.legacySessionHandler();
        GCMHandler gcmHandler = coreRouter.legacyGCMHandler();
        AnalyticsLog.log(context, "ErrorType=LogGroupChatWebSocketError!"
                + " Error=" + error
                + " UserID=" + (sessionHandler.getLoginID()
                .equals("") ? "0" : sessionHandler.getLoginID())
                + " Url=" + "'" + url + "'"
                + " AppPackage=" + GlobalConfig.getPackageApplicationName()
                + " AppVersion=" + GlobalConfig.VERSION_NAME
                + " AppCode=" + GlobalConfig.VERSION_CODE
                + " OSVersion=" + Build.VERSION.RELEASE
                + " DeviceModel=" + android.os.Build.MODEL
                + " DeviceId=" + "'" + gcmHandler.getRegistrationId() + "'"
                + " "

        );
    }


    public static void logNetworkError(Context context, GCMHandler gcmHandler, SessionHandler sessionHandler, String url, int errorCode) {
        String baseUrl = getBaseUrl(url);

        AnalyticsLog.log(context, "ErrorType=Error Network! "
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

    public static void logNotification(Context context, SessionHandler sessionHandler, String notificationId, String notificationCode) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        if (remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.NOTIFICATION_LOGGER, false)) {
            AnalyticsLog.log(context, "Notification Received. User: " + sessionHandler.getLoginID()
                    + " Notification Id: " + notificationId
                    + " Notification Code: " + notificationCode
            );
        }
    }

    private static void log(Context context, String message) {
        try {
            Timber.w(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logInvalidGrant(Context context, GCMHandler gcmHandler, SessionHandler sessionHandler, String url) {
        String baseUrl = getBaseUrl(url);

        AnalyticsLog.log(context, "ErrorType=Invalid Grant!"
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
                + " RefreshToken=" + "'" + (sessionHandler.getRefreshToken()) + "'"
        );
    }
}
