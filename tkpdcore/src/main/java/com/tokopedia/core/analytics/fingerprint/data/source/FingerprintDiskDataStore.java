package com.tokopedia.core.analytics.fingerprint.data.source;

import android.os.Build;

import com.google.gson.Gson;
import com.tokopedia.core.analytics.fingerprint.LocationCache;
import com.tokopedia.core.analytics.fingerprint.Utilities;
import com.tokopedia.core.analytics.fingerprint.data.FingerprintDataStore;
import com.tokopedia.core.analytics.fingerprint.domain.model.FingerPrint;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;

import java.util.Locale;
import java.util.TimeZone;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public class FingerprintDiskDataStore implements FingerprintDataStore {

    @Override
    public Observable<String> getFingerprint(final FingerPrint data) {
        return Observable.just(data)
                .map(new Func1<FingerPrint, FingerPrint>() {
                    @Override
                    public FingerPrint call(FingerPrint fingerPrint) {

                        String deviceID = GCMHandler.getRegistrationId(MainApplication.getAppContext());
                        String deviceName = Build.MODEL;
                        String deviceFabrik = Build.MANUFACTURER;
                        String deviceOS = Build.VERSION.RELEASE;
                        boolean isRooted = Utilities.isDeviceRooted();
                        String timezone = TimeZone.getDefault().getDisplayName();
                        String userAgent = System.getProperty("http.agent");
                        boolean isEmulator = Utilities.isDeviceEmulated();
                        boolean isTablet = Utilities.isDeviceTablet(MainApplication.getAppContext());
                        String buildNumber = Build.FINGERPRINT;
                        String ipAddress = Utilities.getIPAddress(true);
                        String screenReso = Utilities.getScreenResolution(MainApplication.getAppContext());
                        String deviceLanguage = Locale.getDefault().toString();

                        FingerPrint fp = new FingerPrint.FingerPrintBuilder()
                                .deviceID(deviceID)
                                .deviceName(deviceName)
                                .deviceManufacturer(deviceFabrik)
                                .currentOS(deviceOS)
                                .jailbreak(isRooted)
                                .timezone(timezone)
                                .userAgent(userAgent)
                                .emulator(isEmulator)
                                .tablet(isTablet)
                                .buildNumber(buildNumber)
                                .ipAddres(ipAddress)
                                .screenReso(screenReso)
                                .language(deviceLanguage)
//                                .deviceLat(LocationCache.getLocation().getLatitude())
//                                .deviceLng(LocationCache.getLocation().getLongitude())
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
