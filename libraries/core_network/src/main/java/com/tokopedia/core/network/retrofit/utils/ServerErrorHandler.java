package com.tokopedia.core.network.retrofit.utils;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.user.session.UserSession;

/**
 * @author anggaprasetiyo on 5/23/17.
 *         Pindahan dari TkpdAuthInterceptor, biar bisa dipake rame rame
 */

@Deprecated
public class ServerErrorHandler {
    private static final String ACTION_FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    private static final String ACTION_TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";

    public static final String STATUS_UNDER_MAINTENANCE = "UNDER_MAINTENANCE";
    public static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    public static final String STATUS_FORBIDDEN = "FORBIDDEN";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String PATH = "path";
    private static final int THRESHOLD_DIGITS = 10;
    
    public static void showMaintenancePage() {
        CoreNetworkApplication.getAppContext().startActivity(
                CoreNetworkApplication.getCoreNetworkRouter().getMaintenancePageIntent());
    }

    public static void showForceLogoutDialog(String path) {
        Intent intent = new Intent();
        intent.setAction(ACTION_FORCE_LOGOUT);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        if(userSession.getAccessToken().length() > THRESHOLD_DIGITS) {
            intent.putExtra(ACCESS_TOKEN, userSession.getAccessToken().substring(userSession.getAccessToken().length() - THRESHOLD_DIGITS));
        } else {
            intent.putExtra(ACCESS_TOKEN, userSession.getAccessToken());
        }
        intent.putExtra(PATH, path);
        LocalBroadcastManager.getInstance(CoreNetworkApplication.getAppContext()).sendBroadcast(intent);
    }

    public static void sendForceLogoutAnalytics(String url, boolean isInvalidToken, boolean isRequestDenied, String type) {
        AnalyticsLog.logForceLogout(
                CoreNetworkApplication.getAppContext(),
                url,
                isInvalidToken,
                isRequestDenied, type);
    }
    public static void sendForceLogoutAnalytics(String url, boolean isInvalidToken, boolean isRequestDenied) {
        sendForceLogoutAnalytics(url, isInvalidToken, isRequestDenied, "");
    }

    public static void sendErrorNetworkAnalytics(String url, int errorCode) {
        AnalyticsLog.logNetworkError(
                CoreNetworkApplication.getAppContext(),
                url, errorCode);
    }

    public static void showTimezoneErrorSnackbar() {
        Intent intent = new Intent();
        intent.setAction(ACTION_TIMEZONE_ERROR);
        LocalBroadcastManager.getInstance(CoreNetworkApplication.getAppContext()).sendBroadcast(intent);
    }

    public static void sendForceLogoutTokenAnalytics(String url) {
        Context appContext = CoreNetworkApplication.getAppContext();
        AnalyticsLog.logForceLogoutToken(appContext,
                url);
    }
}
