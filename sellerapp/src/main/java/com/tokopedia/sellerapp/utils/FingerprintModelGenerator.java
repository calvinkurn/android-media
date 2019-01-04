package com.tokopedia.sellerapp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.gson.Gson;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.fingerprint.LocationCache;
import com.tokopedia.core.analytics.fingerprint.Utilities;
import com.tokopedia.core.analytics.fingerprint.domain.model.FingerPrint;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.network.data.model.FingerprintModel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author ricoharisin .
 */

public class FingerprintModelGenerator {

    private static final String FINGERPRINT_KEY_NAME = "FINGERPRINT_KEY_NAME";
    private static final String FINGERPRINT_USE_CASE = "FINGERPRINT_USE_CASE";

    public static FingerprintModel generateFingerprintModel(Context context) {
        FingerprintModel fingerprintModel = new FingerprintModel();

        String fingerprintString;

        try {
            fingerprintString = Utilities.toBase64(getFingerPrintJson(context), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            fingerprintString = "";
        }

        fingerprintModel.setAdsId(getGoogleAdId(context));
        fingerprintModel.setFingerprintHash(fingerprintString);
        fingerprintModel.setRegistrarionId(FCMCacheManager.getRegistrationIdWithTemp(context));

        return fingerprintModel;

    }

    private static String getFingerPrintJson(Context context) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, FINGERPRINT_KEY_NAME);
        String cache =  localCacheHandler.getString(FINGERPRINT_USE_CASE);
        if(TextUtils.isEmpty(cache) || localCacheHandler.isExpired()){
            String fingerPrint = generateFingerprintData(context);
            localCacheHandler.putString(FINGERPRINT_USE_CASE, fingerPrint);
            localCacheHandler.setExpire(3600);
            return fingerPrint;
        }

        return cache;
    }

    private static String getGoogleAdId(final Context context) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.ADVERTISINGID);

        String adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID);
        if (adsId != null && !TextUtils.isEmpty(adsId.trim())) {
            return adsId;
        } else {
            AdvertisingIdClient.Info adInfo;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                e.printStackTrace();
                return "";
            }

            if (adInfo != null) {
                String adID = adInfo.getId();

                if (!TextUtils.isEmpty(adID)) {
                    localCacheHandler.putString(TkpdCache.Key.KEY_ADVERTISINGID, adID);
                    localCacheHandler.applyEditor();
                }
                return adID;
            }
        }
        return "";
    }
    
    private static String generateFingerprintData(Context context) {
        String deviceName   = Utilities.getDeviceModel();
        String deviceFabrik = Utilities.getDeviceFabrik();
        String deviceOS     = Utilities.getDeviceOS();
        String deviceSystem = "android";
        boolean isRooted    = Utilities.isDeviceRooted();
        String timezone     = Utilities.getTimeZoneOffset();
        String userAgent    = Utilities.getHttpAgent();
        boolean isEmulator  = Utilities.isDeviceEmulated();
        boolean isTablet    = Utilities.isDeviceTablet(context);
        String screenReso     = Utilities.getScreenResolution(context);
        String deviceLanguage = Utilities.getLanguage();
        String ssid         = Utilities.getSSID(context);
        String carrier      = Utilities.getCarrierName(context);

        FingerPrint fp = new FingerPrint.FingerPrintBuilder()
                .deviceName(deviceName)
                .deviceManufacturer(deviceFabrik)
                .model(deviceName)
                .system(deviceSystem)
                .currentOS(deviceOS)
                .jailbreak(isRooted)
                .timezone(timezone)
                .userAgent(userAgent)
                .emulator(isEmulator)
                .tablet(isTablet)
                .screenReso(screenReso)
                .language(deviceLanguage)
                .ssid(ssid)
                .carrier(carrier)
                .deviceLat(new LocationCache(context).getLatitudeCache())
                .deviceLng(new LocationCache(context).getLongitudeCache())
                .build();

        return new Gson().toJson(fp);
    }


}
