package com.tokopedia.common_tradein.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.user.session.UserSession;

import static android.content.Context.TELEPHONY_SERVICE;

public class TradeInUtils {

    private static String CACHE_IMEI = "CACHE_IMEI";
    private static String IMEI_NUMBER = "IMEI_NUMBER";

    public static String getDeviceId(Context context) {
        try {
            if (getImeiNumber(context) != null) {
                return getImeiNumber(context);
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (!(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == 0)) {
                Bundle tradeInData = ((Activity) context).getIntent().getExtras();
                if (tradeInData != null) {
                    String did = tradeInData.getString("DEVICE ID");
                    if (did != null && did.length() > 0) {
                        return did;
                    } else {
                        UserSession userSession = new UserSession(context);
                        return userSession.getDeviceId();
                    }
                } else {
                    UserSession userSession = new UserSession(context);
                    return userSession.getDeviceId();
                }
            } else {
                String imei = "";
                if (Build.VERSION.SDK_INT >= 26) {
                    imei = telephonyManager.getImei();
                } else {
                    imei = telephonyManager.getDeviceId();
                }
                return imei != null && !imei.isEmpty() ? imei : null;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static void setImeiNumber(Context context, String imeiNumber){
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CACHE_IMEI);
        localCacheHandler.putString(IMEI_NUMBER, imeiNumber);
        localCacheHandler.applyEditor();
    }

    @Nullable
    public static String getImeiNumber(Context context){
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CACHE_IMEI);
        return localCacheHandler.getString(IMEI_NUMBER);
    }
}
