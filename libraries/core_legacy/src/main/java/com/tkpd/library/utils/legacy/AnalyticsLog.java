package com.tkpd.library.utils.legacy;

import android.content.Context;
import android.os.Build;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;

import java.net.MalformedURLException;
import java.net.URL;

import timber.log.Timber;

/**
 * Created by ricoharisin on 8/23/16.
 */
public class AnalyticsLog {
    private static final String EVENT_CLICK_LOGOUT = "clickLogout";
    private static final String CATEGORY_FORCE_LOGOUT = "force logout";

    public static void logForceLogout(Context context, String url, boolean isInvalidToken, boolean isRequestDenied, String type) {
        String baseUrl = getBaseUrl(url);
        GCMHandler gcmHandler = new GCMHandler(context);
        UserSession userSession = new UserSession(context);
        String userId  =userSession.getUserId();

        AnalyticsLog.log(context, "ErrorType=Force Logout!"
                + " UserID=" + (userId
                .equals("") ? "0" : userId)
                + " Url=" + "'" + url + "'"
                + " BaseUrl=" + "'" + baseUrl + "'"
                + " AppPackage=" + GlobalConfig.getPackageApplicationName()
                + " AppVersion=" + GlobalConfig.VERSION_NAME
                + " AppCode=" + GlobalConfig.VERSION_CODE
                + " OSVersion=" + Build.VERSION.RELEASE
                + " DeviceModel=" + Build.MODEL
                + " DeviceId=" + "'" + gcmHandler.getRegistrationId() + "'"
                + " Environment=" + isStaging(baseUrl)
                + " RefreshType= " + "'" + type + "'"

        );

        if (isInvalidToken) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(
                    EVENT_CLICK_LOGOUT,
                    CATEGORY_FORCE_LOGOUT,
                    "get invalid request",
                    baseUrl
            );
        } else if (isRequestDenied) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(
                    EVENT_CLICK_LOGOUT,
                    CATEGORY_FORCE_LOGOUT,
                    "request denied",
                    baseUrl
                    );
        }
    }

    public static void logForceLogoutToken(Context context, String url) {
        String baseUrl = getBaseUrl(url);
        GCMHandler gcmHandler= new GCMHandler(context);
        UserSession userSession = new UserSession(context);
        String userId  =userSession.getUserId();

        AnalyticsLog.log(context, "ErrorType=Force Logout Token!"
                + " UserID=" + (userId
                .equals("") ? "0" : userId)
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
                "get invalid token",
                baseUrl
        );
    }

    public static void logNetworkError(Context context, String url, int errorCode) {
        String baseUrl = getBaseUrl(url);
        GCMHandler gcmHandler= new GCMHandler(context);
        UserSession userSession = new UserSession(context);
        String userId  =userSession.getUserId();

        AnalyticsLog.log(context, "ErrorType=Error Network! "
                + " ErrorCode=" + errorCode
                + " UserID=" + (userId
                .equals("") ? "0" : userId)
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

    public static void logNotification(Context context, String userId, String notificationId, String notificationCode) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        if (remoteConfig.getBoolean(RemoteConfigKey.NOTIFICATION_LOGGER, false)) {
            AnalyticsLog.log(context, "Notification Received. User: " + userId
                    + " Notification Id: " + notificationId
                    + " Notification Code: " + notificationCode
            );
        }
    }

    private static void log(Context context, String message) {
        try {
            Timber.d(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logInvalidGrant(Context context, String url) {
        String baseUrl = getBaseUrl(url);
        GCMHandler gcmHandler = new GCMHandler(context);
        UserSession userSession = new UserSession(context);
        String userId  =userSession.getUserId();
        String refreshToken = userSession.getRefreshTokenIV();

        AnalyticsLog.log(context, "ErrorType=Invalid Grant!"
                + " UserID=" + (userId
                .equals("") ? "0" : userId)
                + " Url=" + "'" + url + "'"
                + " BaseUrl=" + "'" + baseUrl + "'"
                + " AppPackage=" + GlobalConfig.getPackageApplicationName()
                + " AppVersion=" + GlobalConfig.VERSION_NAME
                + " AppCode=" + GlobalConfig.VERSION_CODE
                + " OSVersion=" + Build.VERSION.RELEASE
                + " DeviceModel=" + Build.MODEL
                + " DeviceId=" + "'" + gcmHandler.getRegistrationId() + "'"
                + " Environment=" + isStaging(baseUrl)
                + " RefreshToken=" + "'" + (refreshToken) + "'"
        );
    }
}
