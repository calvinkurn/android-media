package com.tokopedia.core.network.retrofit.interceptors;

import android.content.Context;
import android.os.Build;
import android.util.Base64;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.fingerprint.Utilities;
import com.tokopedia.core.analytics.fingerprint.data.FingerprintDataRepository;
import com.tokopedia.core.analytics.fingerprint.domain.FingerprintRepository;
import com.tokopedia.core.analytics.fingerprint.domain.usecase.GetFingerprintUseCase;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ricoharisin on 3/10/17.
 */

public class FingerprintInterceptor implements Interceptor {

    private static final String KEY_SESSION_ID = "Tkpd-SessionId";
    private static final String KEY_USER_ID = "Tkpd-UserId";
    private static final String KEY_ACC_AUTH = "Accounts-Authorization";
    private static final String KEY_FINGERPRINT_DATA = "Fingerprint-Data";
    private static final String KEY_FINGERPRINT_HASH = "Fingerprint-Hash";
    private static final String BEARER = "Bearer ";
    private static final String KEY_ADSID = "X-GA-ID";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = addFingerPrint(newRequest);

        return chain.proceed(newRequest.build());
    }

    private Request.Builder addFingerPrint(final Request.Builder newRequest) {
        String json = getFingerPrintJson();

        SessionHandler session = new SessionHandler(MainApplication.getAppContext());
        newRequest.addHeader(KEY_SESSION_ID, FCMCacheManager.getRegistrationIdWithTemp(MainApplication.getAppContext()));
        if (session.isV4Login()) {
            newRequest.addHeader(KEY_USER_ID, session.getLoginID());
            newRequest.addHeader(KEY_FINGERPRINT_HASH, AuthUtil.md5(json + "+" + session.getLoginID()));
        } else {
            newRequest.addHeader(KEY_USER_ID, "0");
            newRequest.addHeader(KEY_FINGERPRINT_HASH, AuthUtil.md5(json + "+" + "0"));
        }
        newRequest.addHeader(KEY_ACC_AUTH, BEARER + session.getAccessToken(MainApplication.getAppContext()));
        newRequest.addHeader(KEY_FINGERPRINT_DATA, json);
        newRequest.addHeader(KEY_ADSID, getGoogleAdId(MainApplication.getAppContext()));

        return newRequest;
    }

    private String getFingerPrintJson() {
        String json = "";
        CommonUtils.dumper("Fingerpint is running");
        try {
            GetFingerprintUseCase getFingerprintUseCase;
            FingerprintRepository fpRepo = new FingerprintDataRepository();
            getFingerprintUseCase = new GetFingerprintUseCase(fpRepo);
            json = getFingerprintUseCase.createObservable(RequestParams.EMPTY)
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            return s;
                        }
                    }).map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            try {
                                return Utilities.toBase64(s, Base64.NO_WRAP);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return "UnsupportedEncoding";
                            }

                        }
                    }).doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }).onErrorReturn(new Func1<Throwable, String>() {
                        @Override
                        public String call(Throwable throwable) {
                            return throwable.toString();
                        }
                    }).toBlocking().single();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return json;
    }

    /**
     * This function first try to fetch the Google Ad ID from cache and if not found in cache, it fetches from system and save into the cache
     *
     * @param context
     * @return
     */
    private String getGoogleAdId(final Context context) {
        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.ADVERTISINGID);

        String adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID);
        if (adsId != null && !"".equalsIgnoreCase(adsId.trim())) {
            return adsId;
        }

        return (Observable.just("").subscribeOn(Schedulers.newThread())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String string) {
                        AdvertisingIdClient.Info adInfo = null;
                        try {
                            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                            return adInfo.getId();
                        } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String adID) {
                        if (adID != null) {
                            localCacheHandler.putString(TkpdCache.Key.KEY_ADVERTISINGID, adID);
                            localCacheHandler.applyEditor();
                        }
                    }
                })).toBlocking().single();
    }
}