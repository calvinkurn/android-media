package com.tokopedia.core.network.retrofit.utils;

import android.content.Intent;

import com.tkpd.library.utils.AnalyticsLog;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.app.MainApplication;

/**
 * @author anggaprasetiyo on 5/23/17.
 *         Pindahan dari TkpdAuthInterceptor, biar bisa dipake rame rame
 */
public class ServerErrorHandler {
    private static final String ACTION_FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    private static final String ACTION_TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";
    private static final String ACTION_SERVER_ERROR = "com.tokopedia.tkpd.SERVER_ERROR";

    public static void showMaintenancePage() {
        MainApplication.getAppContext().startActivity(
                MaintenancePage.createIntentFromNetwork(MainApplication.getAppContext()));
    }

    public static void showForceLogoutDialog() {
        Intent intent = new Intent();
        intent.setAction(ACTION_FORCE_LOGOUT);
        MainApplication.getAppContext().sendBroadcast(intent);
    }

    public static void sendForceLogoutAnalytics(String url) {
        AnalyticsLog.logForceLogout(url);
    }

    public static void sendErrorNetworkAnalytics(String url, int errorCode) {
        AnalyticsLog.logNetworkError(url, errorCode);
    }

    public static void showServerErrorSnackbar() {
        Intent intent = new Intent();
        intent.setAction(ACTION_SERVER_ERROR);
        MainApplication.getAppContext().sendBroadcast(intent);
    }

    public static void showTimezoneErrorSnackbar() {
        Intent intent = new Intent();
        intent.setAction(ACTION_TIMEZONE_ERROR);
        MainApplication.getAppContext().sendBroadcast(intent);
    }


}
