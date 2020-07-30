package com.tokopedia.tkpd.utils;

import android.content.Context;
import android.os.Build;
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
import com.tokopedia.device.info.DeviceConnectionInfo;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.device.info.DevicePerformanceInfo;
import com.tokopedia.device.info.DeviceScreenInfo;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import timber.log.Timber;

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

        // temporary fix until moving this into fingerprint library, because some module do not need this ads id
        // handle exception if called from main thread
        try {
            fingerprintModel.setAdsId(getGoogleAdId(context));
        } catch (Exception e) {
            fingerprintModel.setAdsId("");
        }
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
                Timber.w("P2#FINGERPRINT#" + e.toString() + " | " + Build.FINGERPRINT+" | "+  Build.MANUFACTURER + " | "
                        + Build.BRAND + " | "+Build.DEVICE+" | "+Build.PRODUCT+ " | "+Build.MODEL
                        + " | "+Build.TAGS);
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
        Timber.w("P2#FINGERPRINT#" + Build.FINGERPRINT+" | "+  Build.MANUFACTURER + " | "
            + Build.BRAND + " | "+Build.DEVICE+" | "+Build.PRODUCT+ " | "+Build.MODEL
            + " | "+Build.TAGS);

        String deviceName   = DeviceInfo.getModelName();
        String deviceFabrik = DeviceInfo.getManufacturerName();
        String deviceOS     = DeviceInfo.getOSName();
        String deviceSystem = "android";
        boolean isRooted    = DeviceInfo.isRooted();
        String timezone     = DeviceInfo.getTimeZoneOffset();
        String userAgent    = DeviceConnectionInfo.getHttpAgent();
        boolean isEmulator  = DeviceInfo.isEmulated();
        boolean isTablet    = DeviceScreenInfo.isTablet(context);
        String screenReso     = DeviceScreenInfo.getScreenResolution(context);
        String deviceLanguage = DeviceInfo.getLanguage();
        String ssid         = DeviceConnectionInfo.getSSID(context);
        String carrier      = DeviceConnectionInfo.getCarrierName(context);
        String imei         = DeviceInfo.getImei(context);
        String isNakama = "False";
        if(context instanceof UserSessionInterface)
            isNakama = Utilities.isNakama((UserSessionInterface)context);
        String adsId = getGoogleAdId(context);
        String deviceAvailableProcessor = DevicePerformanceInfo.INSTANCE.getAvailableProcessor(context.getApplicationContext());
        String deviceMemoryClass = DevicePerformanceInfo.INSTANCE.getDeviceMemoryClassCapacity(context.getApplicationContext());
        String deviceDpi = DevicePerformanceInfo.INSTANCE.getDeviceDpi(context.getApplicationContext());
        String androidId = DeviceInfo.getAndroidId(context);
        boolean isx86 = DeviceInfo.isx86();
        String packageName = DeviceInfo.getPackageName(context);

        FingerPrint fp = new FingerPrint.FingerPrintBuilder()
                .uniqueId(adsId)
                .isNakama(isNakama)
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
                .availableProcessor(deviceAvailableProcessor)
                .deviceMemoryClassCapacity(deviceMemoryClass)
                .deviceDpi(deviceDpi)
                .androidId(androidId)
                .isx86(isx86)
                .packageName(packageName)
                .imei(imei)
                .build();

        return new Gson().toJson(fp);
    }


}
