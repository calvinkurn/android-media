package com.tokopedia.core.analytics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Herdi_WORK on 05.12.17.
 */

public class ConnectivityUtils {

    public static final String CONN_WIFI = "WI-FI";
    public static final String CONN_RTT = "1xRTT";
    public static final String CONN_CDMA = "CDMA";
    public static final String CONN_EDGE = "EDGE";
    public static final String CONN_EHRPD = "eHRPD";
    public static final String CONN_EVDO_0 = "EVDO_0";
    public static final String CONN_EVDO_A = "EVDO_A";
    public static final String CONN_EVDO_B = "EVDO_B";
    public static final String CONN_GPRS = "GPRS";
    public static final String CONN_GSM = "GSM";
    public static final String CONN_HSDPA = "HSDPA";
    public static final String CONN_HSPA = "HSPA";
    public static final String CONN_HSPAP = "HSPA+";
    public static final String CONN_HSUPA = "HSUPA";
    public static final String CONN_IDEN = "iDen";
    public static final String CONN_UMTS = "UMTS";
    public static final String CONN_LTE = "LTE";
    public static final String CONN_UNKNOWN = "unknown";


    public static boolean isConnected(Context context) {
        NetworkInfo info = ConnectivityUtils.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static String getConnectionType(Context context) {
        NetworkInfo info = ConnectivityUtils.getNetworkInfo(context);
        return ConnectivityUtils.getConnectionType(info.getType(), info.getSubtype());
    }

    public static String getConnectionType(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return CONN_WIFI;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return CONN_RTT; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return CONN_CDMA; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return CONN_EDGE; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return CONN_EVDO_0; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return CONN_EVDO_A; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return CONN_GPRS; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return CONN_HSDPA; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return CONN_HSPA; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return CONN_HSUPA; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return CONN_UMTS; // ~ 400-7000 kbps
            /*
             * Above API level 7
             */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return CONN_EHRPD; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return CONN_EVDO_B; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return CONN_HSPAP; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return CONN_IDEN; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return CONN_LTE; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return CONN_UNKNOWN;
            }
        } else {
            return CONN_UNKNOWN;
        }
    }

}
