package com.tokopedia.core.analytics.fingerprint.data.source;

import android.content.Context;
import android.os.Build;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.gson.Gson;
import com.tokopedia.core.analytics.fingerprint.LocationCache;
import com.tokopedia.core.analytics.fingerprint.data.FingerprintDataStore;
import com.tokopedia.core.analytics.fingerprint.domain.model.FingerPrint;
import com.tokopedia.device.info.DeviceConnectionInfo;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.device.info.DeviceScreenInfo;

import java.io.IOException;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public class FingerprintDiskDataStore implements FingerprintDataStore {

    private Context context;

    public FingerprintDiskDataStore(Context context) {
        this.context = context;
    }

    @Override
    public Observable<String> getFingerprint() {
        return Observable.just(true)
                .map(new Func1<Boolean, FingerPrint>() {
                    @Override
                    public FingerPrint call(Boolean bool) {

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
                        String adsId = getGoogleAdId(context);
                        String androidId = DeviceInfo.getAndroidId(context);
                        String imei = DeviceInfo.getImei(context);
                        boolean isx86 = DeviceInfo.isx86();
                        String packageName = DeviceInfo.getPackageName(context);

                        FingerPrint fp = new FingerPrint.FingerPrintBuilder()
                                .uniqueId(adsId)
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
                                .androidId(androidId)
                                .isx86(isx86)
                                .packageName(packageName)
                                .imei(imei)
                                .build();

                        return fp;
                    }
                }).map(new Func1<FingerPrint, String>() {
                    @Override
                    public String call(FingerPrint fingerPrint) {

                        Gson gson = new Gson();

                        return gson.toJson(fingerPrint);

                    }
                });
    }

    private static String getGoogleAdId(final Context context) {
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
            return adInfo.getId();
        }
        return "";
    }
}
