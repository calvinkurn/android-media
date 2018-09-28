package com.tokopedia.core.analytics.fingerprint.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.analytics.fingerprint.LocationCache;
import com.tokopedia.core.analytics.fingerprint.Utilities;
import com.tokopedia.core.analytics.fingerprint.data.FingerprintDataStore;
import com.tokopedia.core.analytics.fingerprint.domain.model.FingerPrint;

import rx.Observable;
import rx.functions.Func1;

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
}
