package com.tokopedia.abstraction.common.network.factory;

import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.TkpdOkHttpBuilder;

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
        return new TkpdOkHttpBuilder(builder);
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            loggingLevel = HttpLoggingInterceptor.Level.BODY;
//        }
        return new HttpLoggingInterceptor().setLevel(loggingLevel);
    }

    private Interceptor getHeaderHttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            loggingLevel = HttpLoggingInterceptor.Level.HEADERS;
//        }
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

//    public OkHttpClient buildClientAuth(String authKey) {
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new GlobalTkpdAuthInterceptor(authKey))
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .build();
//    }
//
//    public OkHttpClient buildClientDynamicAuth() {
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new DynamicTkpdAuthInterceptor())
//                .addInterceptor(getHttpLoggingInterceptor())
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .build();
//    }
//
//    public OkHttpClient buildClientDefaultCacheAuth() {
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new ApiCacheInterceptor())
//                .addInterceptor(new TkpdAuthInterceptor())
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .build();
//    }
//
//    public OkHttpClient buildClientDefaultAuth() {
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new ApiCacheInterceptor())
//                .addInterceptor(new TkpdAuthInterceptor())
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .build();
//    }
//
    public OkHttpClient buildClientNoAuth() {
        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new TkpdBaseInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }
//
//    public OkHttpClient buildClientAccountsAuth(String authKey, Boolean isUsingHMAC, boolean isUsingBothAuthorization) {
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new AccountsInterceptor(authKey, isUsingHMAC, isUsingBothAuthorization))
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .addInterceptor(getHeaderHttpLoggingInterceptor())
//                .build();
//    }
//
//    public OkHttpClient buildClientBearerAuth(String authorizationString) {
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new StandardizedInterceptor(authorizationString))
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .build();
//    }
//
//    public OkHttpClient buildClientBearerWithClientDefaultAuth() {
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new TkpdBearerWithAuthTypeJsonUtInterceptor())
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .build();
//    }
//
//    public OkHttpClient buildClientDigitalAuth(String authorizationString) {
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new DigitalHmacAuthInterceptor(authorizationString))
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .build();
//    }
//
//    public OkHttpClient buildDaggerClientAuth(FingerprintInterceptor fingerprintInterceptor,
//                                              GlobalTkpdAuthInterceptor globalTkpdAuthInterceptor,
//                                              OkHttpRetryPolicy okHttpRetryPolicy,
//                                              ChuckInterceptor chuckInterceptor,
//                                              DebugInterceptor debugInterceptor,
//                                              ApiCacheInterceptor apiCacheInterceptor) {
//
//        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
//                .addInterceptor(apiCacheInterceptor)
//                .addInterceptor(fingerprintInterceptor)
//                .addInterceptor(globalTkpdAuthInterceptor)
//                .setOkHttpRetryPolicy(okHttpRetryPolicy);
//
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            tkpdbBuilder.addInterceptor(debugInterceptor);
//            tkpdbBuilder.addInterceptor(chuckInterceptor);
//        }
//        return tkpdbBuilder.build();
//    }
//
//    public OkHttpClient buildDaggerClientDefaultAuth(FingerprintInterceptor fingerprintInterceptor,
//                                                     TkpdAuthInterceptor tkpdAuthInterceptor,
//                                                     OkHttpRetryPolicy okHttpRetryPolicy,
//                                                     ChuckInterceptor chuckInterceptor,
//                                                     DebugInterceptor debugInterceptor,
//                                                     ApiCacheInterceptor apiCacheInterceptor) {
//
//        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
//                .addInterceptor(apiCacheInterceptor)
//                .addInterceptor(fingerprintInterceptor)
//                .addInterceptor(tkpdAuthInterceptor)
//                .setOkHttpRetryPolicy(okHttpRetryPolicy);
//
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            tkpdbBuilder.addInterceptor(debugInterceptor);
//            tkpdbBuilder.addInterceptor(chuckInterceptor);
//        }
//        return tkpdbBuilder.build();
//    }
//
//    public OkHttpClient buildDaggerClientBearerAuth(FingerprintInterceptor fingerprintInterceptor,
//                                                    StandardizedInterceptor standardizedInterceptor,
//                                                    OkHttpRetryPolicy okHttpRetryPolicy,
//                                                    ChuckInterceptor chuckInterceptor,
//                                                    DebugInterceptor debugInterceptor) {
//
//        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
//                .addInterceptor(fingerprintInterceptor)
//                .addInterceptor(standardizedInterceptor)
//                .setOkHttpRetryPolicy(okHttpRetryPolicy);
//
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            tkpdbBuilder.addInterceptor(debugInterceptor);
//            tkpdbBuilder.addInterceptor(chuckInterceptor);
//        }
//        return tkpdbBuilder.build();
//    }
//
//    public OkHttpClient buildDaggerClientNoAuth(FingerprintInterceptor fingerprintInterceptor,
//                                                TkpdBaseInterceptor tkpdBaseInterceptor,
//                                                OkHttpRetryPolicy okHttpRetryPolicy,
//                                                ChuckInterceptor chuckInterceptor,
//                                                DebugInterceptor debugInterceptor,
//                                                ApiCacheInterceptor apiCacheInterceptor) {
//
//        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
//                .addInterceptor(fingerprintInterceptor)
//                .addInterceptor(tkpdBaseInterceptor)
//                .addInterceptor(apiCacheInterceptor)
//                .setOkHttpRetryPolicy(okHttpRetryPolicy);
//
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            tkpdbBuilder.addInterceptor(debugInterceptor);
//            tkpdbBuilder.addInterceptor(chuckInterceptor);
//        }
//        return tkpdbBuilder.build();
//    }
//
//    public OkHttpClient buildDaggerClientNoAuthNoFingerPrint(TkpdBaseInterceptor tkpdBaseInterceptor,
//                                                             OkHttpRetryPolicy okHttpRetryPolicy,
//                                                             ChuckInterceptor chuckInterceptor,
//                                                             DebugInterceptor debugInterceptor) {
//
//        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
//                .addInterceptor(tkpdBaseInterceptor)
//                .setOkHttpRetryPolicy(okHttpRetryPolicy);
//
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            tkpdbBuilder.addInterceptor(debugInterceptor);
//            tkpdbBuilder.addInterceptor(chuckInterceptor);
//        }
//        return tkpdbBuilder.build();
//    }
//
//    public OkHttpClient buildDaggerClientBearerRidehailing(RideInterceptor rideInterceptor,
//                                                           OkHttpRetryPolicy okHttpRetryPolicy,
//                                                           ChuckInterceptor chuckInterceptor,
//                                                           DebugInterceptor debugInterceptor,
//                                                           HttpLoggingInterceptor loggingInterceptor) {
//        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(builder)
//                .addInterceptor(rideInterceptor)
//                .setOkHttpRetryPolicy(okHttpRetryPolicy);
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            tkpdOkHttpBuilder.addInterceptor(debugInterceptor);
//            tkpdOkHttpBuilder.addInterceptor(chuckInterceptor);
//            tkpdOkHttpBuilder.addInterceptor(loggingInterceptor);
//        }
//        return tkpdOkHttpBuilder.build();
//    }
//
//    public OkHttpClient buildDaggerClientBearerWithClientDefaultAuth(TkpdBearerWithAuthTypeJsonUtInterceptor tkpdBearerWithAuthTypeJsonUtInterceptor,
//                                                                     OkHttpRetryPolicy okHttpRetryPolicy,
//                                                                     ChuckInterceptor chuckInterceptor,
//                                                                     DebugInterceptor debugInterceptor,
//                                                                     ApiCacheInterceptor apiCacheInterceptor) {
//
//        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
//                .addInterceptor(tkpdBearerWithAuthTypeJsonUtInterceptor)
//                .addInterceptor(apiCacheInterceptor)
//                .addInterceptor(new TkpdErrorResponseInterceptor(TkpdV4ResponseError.class))
//                .setOkHttpRetryPolicy(okHttpRetryPolicy);
//
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            tkpdbBuilder.addInterceptor(debugInterceptor);
//            tkpdbBuilder.addInterceptor(chuckInterceptor);
//        }
//        return tkpdbBuilder.build();
//    }
//
//
//    public OkHttpClient buildDaggerClientDefaultAuthWithErrorHandler(FingerprintInterceptor fingerprintInterceptor,
//                                                                     TkpdAuthInterceptor tkpdAuthInterceptor,
//                                                                     OkHttpRetryPolicy okHttpRetryPolicy,
//                                                                     ChuckInterceptor chuckInterceptor,
//                                                                     DebugInterceptor debugInterceptor,
//                                                                     Interceptor tkpdErrorHandlerInterceptor) {
//        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
//                .addInterceptor(fingerprintInterceptor)
//                .addInterceptor(tkpdAuthInterceptor)
//                .addInterceptor(tkpdErrorHandlerInterceptor)
//                .setOkHttpRetryPolicy(okHttpRetryPolicy);
//
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            tkpdbBuilder.addInterceptor(debugInterceptor);
//            tkpdbBuilder.addInterceptor(chuckInterceptor);
//        }
//        return tkpdbBuilder.build();
//    }
//
//    public OkHttpClient buildDaggerClientResolutionAuth(FingerprintInterceptor fingerprintInterceptor,
//                                                        ResolutionInterceptor resolutionInterceptor,
//                                                        OkHttpRetryPolicy okHttpRetryPolicy,
//                                                        ChuckInterceptor chuckInterceptor,
//                                                        DebugInterceptor debugInterceptor) {
//        TkpdOkHttpBuilder tkpdbBuilder = new TkpdOkHttpBuilder(builder)
//                .addInterceptor(fingerprintInterceptor)
//                .addInterceptor(resolutionInterceptor)
//                .setOkHttpRetryPolicy(okHttpRetryPolicy);
//
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            tkpdbBuilder.addInterceptor(debugInterceptor);
//            tkpdbBuilder.addInterceptor(chuckInterceptor);
//        }
//        return tkpdbBuilder.build();
//    }
//
//    public OkHttpClient buildClientTokoCashAuth(String authKey) {
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new MsisdnInterceptor(authKey))
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .build();
//    }
//
//    public OkHttpClient buildClientCreditCardAuth() {
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new CreditCardInterceptor())
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .build();
//    }

}
