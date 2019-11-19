package com.tokopedia.core.network.core;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.retrofit.interceptors.AccountsBasicInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.AccountsInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.CreditCardInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.DigitalHmacAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.DynamicTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.ReactNativeBearerInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.ReactNativeInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBearerWithAuthTypeJsonUtInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TopAdsAuthInterceptor;
import com.tokopedia.core.network.retrofit.response.TkpdV4ResponseError;
import com.tokopedia.core.network.retrofit.response.TopAdsResponseError;
import com.tokopedia.core.util.GlobalConfig;

import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 2/27/17.
 */

@Deprecated
public class OkHttpFactory {

    private final OkHttpClient client = getDefaultClient();
    protected OkHttpRetryPolicy okHttpRetryPolicy;
    protected OkHttpClient.Builder builder;

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
        return new TkpdOkHttpBuilder(builder).addInterceptor(getHttpLoggingInterceptor());
    }

    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.BODY;
        }
        return new HttpLoggingInterceptor().setLevel(loggingLevel);
    }

    private Interceptor getHeaderHttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.HEADERS;
        }
        return new HttpLoggingInterceptor().setLevel(loggingLevel);
    }

    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
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
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new GlobalTkpdAuthInterceptor(authKey))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientDynamicAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new DynamicTkpdAuthInterceptor())
                .addInterceptor(getHttpLoggingInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientReactNativeNoAuth(HashMap<String, String> headers) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new ReactNativeInterceptor(headers))
                .addInterceptor(new TkpdBaseInterceptor())
                .addInterceptor(getHttpLoggingInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientReactNativeAuth(HashMap<String, String> headers) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new ReactNativeInterceptor(headers))
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new DynamicTkpdAuthInterceptor())
                .addInterceptor(getHttpLoggingInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientReactNativeBearer(HashMap<String, String> headers, String token) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new ReactNativeBearerInterceptor(headers, token))
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new DynamicTkpdAuthInterceptor())
                .addInterceptor(getHttpLoggingInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientDefaultCacheAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new CacheApiInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new TkpdAuthInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    public OkHttpClient buildClientDefaultAuth() {
        return buildClientDefaultAuthBuilder().build();
    }

    public TkpdOkHttpBuilder buildClientDefaultAuthBuilder() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new CacheApiInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new TkpdAuthInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor();
    }

    public OkHttpClient buildClientNoAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new TkpdBaseInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientAccountsAuth(String authKey, Boolean isUsingHMAC, boolean isUsingBothAuthorization) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new AccountsInterceptor(authKey, isUsingHMAC,
                        isUsingBothAuthorization))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .addInterceptor(getHttpLoggingInterceptor())
                .build();
    }

    public OkHttpClient buildBasicAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new AccountsBasicInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .addInterceptor(getHttpLoggingInterceptor())
                .build();
    }

    public OkHttpClient buildClientBearerAuth(String authorizationString) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new StandardizedInterceptor(authorizationString))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientDigitalAuth(String authorizationString) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new DigitalHmacAuthInterceptor(authorizationString))
                .addInterceptor(getHttpLoggingInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildDaggerClientAuth(FingerprintInterceptor fingerprintInterceptor,
                                              GlobalTkpdAuthInterceptor globalTkpdAuthInterceptor,
                                              OkHttpRetryPolicy okHttpRetryPolicy,
                                              ChuckInterceptor chuckInterceptor,
                                              DebugInterceptor debugInterceptor,
                                              CacheApiInterceptor cacheApiInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(cacheApiInterceptor)
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
                                                     DebugInterceptor debugInterceptor,
                                                     CacheApiInterceptor cacheApiInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(cacheApiInterceptor)
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
                                                DebugInterceptor debugInterceptor,
                                                CacheApiInterceptor cacheApiInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdBaseInterceptor)
                .addInterceptor(cacheApiInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy);

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor);
            tkpdbBuilder.addInterceptor(chuckInterceptor);
        }
        return tkpdbBuilder.build();
    }

    public OkHttpClient buildDaggerClientNoAuthWithBearer(
            TopAdsAuthInterceptor bearerWithAuthInterceptor,
            FingerprintInterceptor fingerprintInterceptor,
            TkpdBaseInterceptor tkpdBaseInterceptor,
            OkHttpRetryPolicy okHttpRetryPolicy,
            ChuckInterceptor chuckInterceptor,
            DebugInterceptor debugInterceptor,
            CacheApiInterceptor cacheApiInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(bearerWithAuthInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdBaseInterceptor)
                .addInterceptor(cacheApiInterceptor)
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

    public OkHttpClient buildDaggerClientBearerWithClientDefaultAuth(TkpdBearerWithAuthTypeJsonUtInterceptor tkpdBearerWithAuthTypeJsonUtInterceptor,
                                                                     OkHttpRetryPolicy okHttpRetryPolicy,
                                                                     ChuckInterceptor chuckInterceptor,
                                                                     DebugInterceptor debugInterceptor,
                                                                     CacheApiInterceptor cacheApiInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(tkpdBearerWithAuthTypeJsonUtInterceptor)
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(new TkpdErrorResponseInterceptor(TkpdV4ResponseError.class))
                .setOkHttpRetryPolicy(okHttpRetryPolicy);

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor);
            tkpdbBuilder.addInterceptor(chuckInterceptor);
        }
        return tkpdbBuilder.build();
    }


    public OkHttpClient buildDaggerClientDefaultAuthWithErrorHandler(FingerprintInterceptor fingerprintInterceptor,
                                                                     TkpdAuthInterceptor tkpdAuthInterceptor,
                                                                     OkHttpRetryPolicy okHttpRetryPolicy,
                                                                     ChuckInterceptor chuckInterceptor,
                                                                     DebugInterceptor debugInterceptor,
                                                                     Interceptor tkpdErrorHandlerInterceptor,
                                                                     CacheApiInterceptor cacheApiInterceptor) {
        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(tkpdErrorHandlerInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy);

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor);
            tkpdbBuilder.addInterceptor(chuckInterceptor);
        }
        return tkpdbBuilder.build();
    }

    public OkHttpClient buildDaggerClientResolutionAuth(FingerprintInterceptor fingerprintInterceptor,
                                                        TkpdAuthInterceptor resolutionInterceptor,
                                                        OkHttpRetryPolicy okHttpRetryPolicy,
                                                        ChuckInterceptor chuckInterceptor,
                                                        DebugInterceptor debugInterceptor) {
        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(resolutionInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy);

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor);
            tkpdbBuilder.addInterceptor(chuckInterceptor);
        }
        return tkpdbBuilder.build();
    }

    public OkHttpClient buildClientCreditCardAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new CreditCardInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildDaggerClientBearerTopAdsAuth(FingerprintInterceptor fingerprintInterceptor,
                                                          TopAdsAuthInterceptor topAdsAuthInterceptor,
                                                          OkHttpRetryPolicy okHttpRetryPolicy,
                                                          @TopAdsQualifier TkpdErrorResponseInterceptor errorResponseInterceptor,
                                                          CacheApiInterceptor cacheApiInterceptor) {

        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy)
                .addDebugInterceptor()
                .build();
    }

    @Deprecated
    public OkHttpClient buildClientTopAdsAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new CacheApiInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new TopAdsAuthInterceptor())
                .addInterceptor(new TkpdErrorResponseInterceptor(TopAdsResponseError.class))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientTopChatAuth(String authorizationString) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new CacheApiInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new DigitalHmacAuthInterceptor(authorizationString))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient.Builder getClientBuilder() {
        return builder == null ? client.newBuilder() : builder;
    }
}
