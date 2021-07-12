package com.tokopedia.core.network.core;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.retrofit.interceptors.AccountsBasicInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.AccountsInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.CreditCardInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.DigitalHmacAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBearerWithAuthTypeJsonUtInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TopAdsAuthInterceptor;
import com.tokopedia.core.network.retrofit.response.TkpdV4ResponseError;
import com.tokopedia.core.network.retrofit.response.TopAdsResponseError;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 2/27/17.
 */

@Deprecated
public class OkHttpFactory {

    private final OkHttpClient client = getDefaultClient();
    private OkHttpRetryPolicy okHttpRetryPolicy;
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

    public OkHttpClient buildClientDefaultAuth() {
        return buildClientDefaultAuthBuilder().build();
    }

    public TkpdOkHttpBuilder buildClientDefaultAuthBuilder() {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
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
                                              ChuckerInterceptor chuckInterceptor,
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
                                                     ChuckerInterceptor chuckInterceptor,
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
                                                    ChuckerInterceptor chuckInterceptor,
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
                                                ChuckerInterceptor chuckInterceptor,
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

    public OkHttpClient buildDaggerClientNoAuthWithBearer(
            TopAdsAuthInterceptor bearerWithAuthInterceptor,
            FingerprintInterceptor fingerprintInterceptor,
            TkpdBaseInterceptor tkpdBaseInterceptor,
            OkHttpRetryPolicy okHttpRetryPolicy,
            ChuckerInterceptor chuckInterceptor,
            DebugInterceptor debugInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(bearerWithAuthInterceptor)
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
                                                             ChuckerInterceptor chuckInterceptor,
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
                                                                     ChuckerInterceptor chuckInterceptor,
                                                                     DebugInterceptor debugInterceptor) {

        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(tkpdBearerWithAuthTypeJsonUtInterceptor)
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
                                                                     ChuckerInterceptor chuckInterceptor,
                                                                     DebugInterceptor debugInterceptor,
                                                                     Interceptor tkpdErrorHandlerInterceptor) {
        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
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
                                                        ChuckerInterceptor chuckInterceptor,
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
                                                          @TopAdsQualifier TkpdErrorResponseInterceptor errorResponseInterceptor) {

        return new TkpdOkHttpBuilder(builder)
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
                .addInterceptor(new FingerprintInterceptor(CoreNetworkApplication.getAppContext()))
                .addInterceptor(new TopAdsAuthInterceptor())
                .addInterceptor(new TkpdErrorResponseInterceptor(TopAdsResponseError.class))
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }
}