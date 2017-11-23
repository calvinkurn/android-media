package com.tokopedia.flightmockapp;

/**
 * @author anggaprasetiyo on 5/23/17.
 *         Pindahan dari TkpdAuthInterceptor, biar bisa dipake rame rame
 */
public class ServerErrorHandler {
    private static final String ACTION_FORCE_LOGOUT = "com.tokopedia.tkpd.FORCE_LOGOUT";
    private static final String ACTION_TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";
    private static final String ACTION_SERVER_ERROR = "com.tokopedia.tkpd.SERVER_ERROR";

    public static final String STATUS_UNDER_MAINTENANCE = "UNDER_MAINTENANCE";
    public static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    public static final String STATUS_FORBIDDEN = "FORBIDDEN";
    
    public static void showMaintenancePage() {
    }

    public static void showForceLogoutDialog() {
    }

    public static void sendForceLogoutAnalytics(String url) {

    }

    public static void sendErrorNetworkAnalytics(String url, int errorCode) {
    }

    public static void showServerErrorSnackbar() {
    }

    public static void showTimezoneErrorSnackbar() {
    }


}
