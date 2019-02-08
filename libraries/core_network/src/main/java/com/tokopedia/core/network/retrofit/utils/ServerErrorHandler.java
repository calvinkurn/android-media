package com.tokopedia.core.network.retrofit.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tokopedia.core.network.CoreNetworkApplication;

/**
 * @author anggaprasetiyo on 5/23/17.
 *         Pindahan dari TkpdAuthInterceptor, biar bisa dipake rame rame
 */

@Deprecated
public class ServerErrorHandler {
    private static final String ACTION_FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    private static final String ACTION_TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";
    private static final String ACTION_SERVER_ERROR = "com.tokopedia.tkpd.SERVER_ERROR";
    public static final String ACTION_FORCE_HOCKEYAPP = "com.tokopedia.tkpd.FORCE_HOCKEYAPP";

    public static final String STATUS_UNDER_MAINTENANCE = "UNDER_MAINTENANCE";
    public static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    public static final String STATUS_FORBIDDEN = "FORBIDDEN";
    
    public static void showMaintenancePage() {
        CoreNetworkApplication.getAppContext().startActivity(
                CoreNetworkApplication.getCoreNetworkRouter().getMaintenancePageIntent());
    }

    public static void showForceLogoutDialog() {
        Intent intent = new Intent();
        intent.setAction(ACTION_FORCE_LOGOUT);
        LocalBroadcastManager.getInstance(CoreNetworkApplication.getAppContext()).sendBroadcast(intent);
    }

    public static void sendForceLogoutAnalytics(String url) {
        AnalyticsLog.logForceLogout(
                CoreNetworkApplication.getAppContext(),
                CoreNetworkApplication.getCoreNetworkRouter().legacyGCMHandler(),
                CoreNetworkApplication.getCoreNetworkRouter().legacySessionHandler(),
                url);
    }

    public static void sendErrorNetworkAnalytics(String url, int errorCode) {
        AnalyticsLog.logNetworkError(
                CoreNetworkApplication.getAppContext(),
                CoreNetworkApplication.getCoreNetworkRouter().legacyGCMHandler(),
                CoreNetworkApplication.getCoreNetworkRouter().legacySessionHandler(),
                url, errorCode);
    }

    public static void showServerErrorSnackbar() {
        Intent intent = new Intent();
        intent.setAction(ACTION_SERVER_ERROR);
        LocalBroadcastManager.getInstance(CoreNetworkApplication.getAppContext()).sendBroadcast(intent);
    }

    public static void showTimezoneErrorSnackbar() {
        Intent intent = new Intent();
        intent.setAction(ACTION_TIMEZONE_ERROR);
        LocalBroadcastManager.getInstance(CoreNetworkApplication.getAppContext()).sendBroadcast(intent);
    }

    public static void sendForceLogoutTokenAnalytics(String url) {
        Context appContext = CoreNetworkApplication.getAppContext();
        AnalyticsLog.logForceLogoutToken(appContext,
                CoreNetworkApplication.getCoreNetworkRouter().legacyGCMHandler(),
                CoreNetworkApplication.getCoreNetworkRouter().legacySessionHandler(),
                url);
    }
}
