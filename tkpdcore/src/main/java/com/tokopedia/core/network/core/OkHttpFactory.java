package com.tokopedia.core.network.core;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.AccountsInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TopAdsAuthInterceptor;
import com.tokopedia.core.network.retrofit.response.TkpdResponseError;
import com.tokopedia.core.util.GlobalConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 2/27/17.
 */

public class OkHttpFactory {

    private final OkHttpClient client = getDefaultClient();
    private OkHttpRetryPolicy okHttpRetryPolicy;
    private OkHttpClient.Builder builder;

    public OkHttpFactory() {
        builder = client.newBuilder();
    }

    public static OkHttpFactory create() {
        return new OkHttpFactory();
    }

    private OkHttpClient getDefaultClient() {
        return getDefaultClientConfig(new OkHttpClient.Builder())
                .build();
    }

    private TkpdOkHttpBuilder getDefaultClientConfig() {
        return getDefaultClientConfig(builder);
    }

    private TkpdOkHttpBuilder getDefaultClientConfig(OkHttpClient.Builder builder) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(getHttpLoggingInterceptor());
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private OkHttpRetryPolicy getOkHttpRetryPolicy() {
        if (okHttpRetryPolicy == null) {
            return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        }

        return okHttpRetryPolicy;
    }

    public OkHttpFactory addOkHttpRetryPolicy(OkHttpRetryPolicy okHttpRetryPolicy) {
        this.okHttpRetryPolicy = okHttpRetryPolicy;
        return this;
    }

    public OkHttpClient buildClientAuth(String authKey) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new GlobalTkpdAuthInterceptor(authKey))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientDefaultAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new TkpdAuthInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientNoAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new TkpdBaseInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientTopAdsAuth(String authorizationString) {
        return new TkpdOkHttpBuilder(builder)
        		.addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new TopAdsAuthInterceptor(authorizationString))
                .addInterceptor(new TkpdErrorResponseInterceptor(TkpdResponseError.class))
                .addInterceptor(getHttpLoggingInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientAccountsAuth(String authKey, Boolean isUsingHMAC, boolean isUsingBothAuthorization) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new AccountsInterceptor(authKey, isUsingHMAC, isUsingBothAuthorization))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientBearerAuth(String authorizationString) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new StandardizedInterceptor(authorizationString))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildDaggerClientAuth(FingerprintInterceptor fingerprintInterceptor,
                                              GlobalTkpdAuthInterceptor globalTkpdAuthInterceptor,
                                              OkHttpRetryPolicy okHttpRetryPolicy,
                                              ChuckInterceptor chuckInterceptor,
                                              DebugInterceptor debugInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(globalTkpdAuthInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy);

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor);
            tkpdbBuilder.addInterceptor(chuckInterceptor);
        }
        return tkpdbBuilder.build();
    }

    public OkHttpClient buildDaggerClientDefaultAuth(FingerprintInterceptor fingerprintInterceptor,
                                                     TkpdAuthInterceptor tkpdAuthInterceptor,
                                                     OkHttpRetryPolicy okHttpRetryPolicy,
                                                     ChuckInterceptor chuckInterceptor,
                                                     DebugInterceptor debugInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy);

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor);
            tkpdbBuilder.addInterceptor(chuckInterceptor);
        }
        return tkpdbBuilder.build();
    }

    public OkHttpClient buildDaggerClientBearerAuth(FingerprintInterceptor fingerprintInterceptor,
                                                     StandardizedInterceptor standardizedInterceptor,
                                                     OkHttpRetryPolicy okHttpRetryPolicy,
                                                     ChuckInterceptor chuckInterceptor,
                                                     DebugInterceptor debugInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(standardizedInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy);

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor);
            tkpdbBuilder.addInterceptor(chuckInterceptor);
        }
        return tkpdbBuilder.build();
    }

    public OkHttpClient buildDaggerClientNoAuth(FingerprintInterceptor fingerprintInterceptor,
                                                TkpdBaseInterceptor tkpdBaseInterceptor,
                                                OkHttpRetryPolicy okHttpRetryPolicy,
                                                ChuckInterceptor chuckInterceptor,
                                                DebugInterceptor debugInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdBaseInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy);

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor);
            tkpdbBuilder.addInterceptor(chuckInterceptor);
        }
        return tkpdbBuilder.build();
    }

    public OkHttpClient buildDaggerClientNoAuthNoFingerPrint(TkpdBaseInterceptor tkpdBaseInterceptor,
                                                OkHttpRetryPolicy okHttpRetryPolicy,
                                                ChuckInterceptor chuckInterceptor,
                                                DebugInterceptor debugInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(tkpdBaseInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy);

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor);
            tkpdbBuilder.addInterceptor(chuckInterceptor);
        }
        return tkpdbBuilder.build();
    }





}
