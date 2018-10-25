package com.tokopedia.core.network.retrofit.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.GCMHandler;

/**
 * @author anggaprasetiyo on 5/23/17.
 *         Pindahan dari TkpdAuthInterceptor, biar bisa dipake rame rame
 */
public class ServerErrorHandler {
    private static final String ACTION_FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    private static final String ACTION_TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";
    private static final String ACTION_SERVER_ERROR = "com.tokopedia.tkpd.SERVER_ERROR";
    public static final String ACTION_FORCE_HOCKEYAPP = "com.tokopedia.tkpd.FORCE_HOCKEYAPP";

    public static final String STATUS_UNDER_MAINTENANCE = "UNDER_MAINTENANCE";
    public static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    public static final String STATUS_FORBIDDEN = "FORBIDDEN";
    
    public static void showMaintenancePage() {
        MainApplication.getAppContext().startActivity(
                MaintenancePage.createIntentFromNetwork(MainApplication.getAppContext()));
    }

    public static void showForceLogoutDialog() {
        Intent intent = new Intent();
        intent.setAction(ACTION_FORCE_LOGOUT);
        LocalBroadcastManager.getInstance(MainApplication.getAppContext()).sendBroadcast(intent);
    }

    public static void sendForceLogoutAnalytics(String url) {
        AnalyticsLog.logForceLogout(
                MainApplication.getAppContext(),
                MainApplication.getTkpdCoreRouter().legacyGCMHandler(),
                MainApplication.getTkpdCoreRouter().legacySessionHandler(),
                url);
    }

    public static void sendErrorNetworkAnalytics(String url, int errorCode) {
        AnalyticsLog.logNetworkError(
                MainApplication.getAppContext(),
                MainApplication.getTkpdCoreRouter().legacyGCMHandler(),
                MainApplication.getTkpdCoreRouter().legacySessionHandler(),
                url, errorCode);
    }

    public static void showServerErrorSnackbar() {
        Intent intent = new Intent();
        intent.setAction(ACTION_SERVER_ERROR);
        LocalBroadcastManager.getInstance(MainApplication.getAppContext()).sendBroadcast(intent);
    }

    public static void showTimezoneErrorSnackbar() {
        Intent intent = new Intent();
        intent.setAction(ACTION_TIMEZONE_ERROR);
        LocalBroadcastManager.getInstance(MainApplication.getAppContext()).sendBroadcast(intent);
    }

    public static void showForceHockeyAppDialog() {
        Intent intent = new Intent();
        intent.setAction(ACTION_FORCE_HOCKEYAPP);
        LocalBroadcastManager.getInstance(MainApplication.getAppContext()).sendBroadcast(intent);
    }

    public static void sendForceLogoutTokenAnalytics(String url) {
        Context appContext = MainApplication.getAppContext();
        AnalyticsLog.logForceLogoutToken(appContext,
                MainApplication.getTkpdCoreRouter().legacyGCMHandler(),
                MainApplication.getTkpdCoreRouter().legacySessionHandler(),
                url);
    }
}
