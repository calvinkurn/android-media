package com.tokopedia.core.network.core;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.cache.interceptor.ApiCacheInterceptor;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.retrofit.interceptors.AccountsInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.CampaignInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.CreditCardInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.DigitalHmacAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.DynamicTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.MsisdnInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.ReactNativeBearerInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.ReactNativeInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.ResolutionInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.RideInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBearerWithAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBearerWithAuthTypeJsonUtInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TopAdsAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.WalletAuthInterceptor;
import com.tokopedia.core.network.retrofit.response.TkpdV4ResponseError;
import com.tokopedia.core.network.retrofit.response.TopAdsResponseError;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 2/27/17.
 */

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

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
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
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new GlobalTkpdAuthInterceptor(authKey))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientDynamicAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
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
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new DynamicTkpdAuthInterceptor())
                .addInterceptor(getHttpLoggingInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientReactNativeBearer(HashMap<String, String> headers, String token) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new ReactNativeBearerInterceptor(headers, token))
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new DynamicTkpdAuthInterceptor())
                .addInterceptor(getHttpLoggingInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientDefaultCacheAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new ApiCacheInterceptor())
                .addInterceptor(new TkpdAuthInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    public OkHttpClient buildClientDefaultAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new ApiCacheInterceptor())
                .addInterceptor(new TkpdAuthInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientCampaignAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new ApiCacheInterceptor())
                .addInterceptor(new CampaignInterceptor())
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

    public OkHttpClient buildClientAccountsAuth(String authKey, Boolean isUsingHMAC, boolean isUsingBothAuthorization, boolean isBasic) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new AccountsInterceptor(authKey, isUsingHMAC,
                        isUsingBothAuthorization, isBasic))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .addInterceptor(getHttpLoggingInterceptor())
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

    public OkHttpClient buildClientBearerWalletAuth(String authKey) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new WalletAuthInterceptor(authKey))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientBearerWithClientDefaultAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new TkpdBearerWithAuthTypeJsonUtInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientDigitalAuth(String authorizationString) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
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
                                              ApiCacheInterceptor apiCacheInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(apiCacheInterceptor)
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
                                                     ApiCacheInterceptor apiCacheInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(apiCacheInterceptor)
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
                                                ApiCacheInterceptor apiCacheInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdBaseInterceptor)
                .addInterceptor(apiCacheInterceptor)
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
            ApiCacheInterceptor apiCacheInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(bearerWithAuthInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdBaseInterceptor)
                .addInterceptor(apiCacheInterceptor)
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

    public OkHttpClient buildDaggerClientBearerRidehailing(RideInterceptor rideInterceptor,
                                                           OkHttpRetryPolicy okHttpRetryPolicy,
                                                           ChuckInterceptor chuckInterceptor,
                                                           DebugInterceptor debugInterceptor,
                                                           HttpLoggingInterceptor loggingInterceptor) {
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(rideInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy);
        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdOkHttpBuilder.addInterceptor(debugInterceptor);
            tkpdOkHttpBuilder.addInterceptor(chuckInterceptor);
            tkpdOkHttpBuilder.addInterceptor(loggingInterceptor);
        }
        return tkpdOkHttpBuilder.build();
    }

    public OkHttpClient buildDaggerClientBearerWithClientDefaultAuth(TkpdBearerWithAuthTypeJsonUtInterceptor tkpdBearerWithAuthTypeJsonUtInterceptor,
                                                                     OkHttpRetryPolicy okHttpRetryPolicy,
                                                                     ChuckInterceptor chuckInterceptor,
                                                                     DebugInterceptor debugInterceptor,
                                                                     ApiCacheInterceptor apiCacheInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(tkpdBearerWithAuthTypeJsonUtInterceptor)
                .addInterceptor(apiCacheInterceptor)
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
                                                                     ApiCacheInterceptor apiCacheInterceptor) {
        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(apiCacheInterceptor)
                .addInterceptor(tkpdErrorHandlerInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy);

        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdbBuilder.addInterceptor(debugInterceptor);
            tkpdbBuilder.addInterceptor(chuckInterceptor);
        }
        return tkpdbBuilder.build();
    }

    public OkHttpClient buildDaggerClientResolutionAuth(FingerprintInterceptor fingerprintInterceptor,
                                                        ResolutionInterceptor resolutionInterceptor,
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

    public OkHttpClient buildClientTokoCashAuth(String authKey) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new MsisdnInterceptor(authKey))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())

                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientCreditCardAuth() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new CreditCardInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildDaggerClientBearerTopAdsAuth(FingerprintInterceptor fingerprintInterceptor,
                                                          TopAdsAuthInterceptor topAdsAuthInterceptor,
                                                          OkHttpRetryPolicy okHttpRetryPolicy,
                                                          @TopAdsQualifier TkpdErrorResponseInterceptor errorResponseInterceptor,
                                                          ApiCacheInterceptor apiCacheInterceptor) {

        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(apiCacheInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .setOkHttpRetryPolicy(okHttpRetryPolicy)
                .addDebugInterceptor()
                .build();
    }

    @Deprecated
    public OkHttpClient buildClientTopAdsAuth(SessionHandler sessionHandler) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new ApiCacheInterceptor())
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new TopAdsAuthInterceptor(sessionHandler))
                .addInterceptor(new TkpdErrorResponseInterceptor(TopAdsResponseError.class))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient buildClientTopChatAuth(String authorizationString) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor())
                .addInterceptor(new ApiCacheInterceptor())
                .addInterceptor(new DigitalHmacAuthInterceptor(authorizationString))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }

    public OkHttpClient.Builder getClientBuilder() {
        return builder == null ? client.newBuilder() : builder;
    }
}
