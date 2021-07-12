package com.tokopedia.core.network.di.module;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.di.qualifier.BearerAuth;
import com.tokopedia.core.network.di.qualifier.BearerAuthTypeJsonUt;
import com.tokopedia.core.network.di.qualifier.DefaultAuth;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.MojitoAuth;
import com.tokopedia.core.network.di.qualifier.MojitoNoRetryAuth;
import com.tokopedia.core.network.di.qualifier.MojitoSmallTimeoutNoAuth;
import com.tokopedia.core.network.di.qualifier.NoAuth;
import com.tokopedia.core.network.di.qualifier.NoAuthNoFingerprint;
import com.tokopedia.core.network.di.qualifier.TomeBearerAuth;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.di.qualifier.UploadWsV4Auth;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBearerWithAuthTypeJsonUtInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TopAdsAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author  ricoharisin on 3/23/17.
 */

@Deprecated
@Module(includes={InterceptorModule.class})
public class OkHttpClientModule {

    @TomeBearerAuth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientTomeBearerAuth(HttpLoggingInterceptor httpLoggingInterceptor,
                                                          BearerInterceptor bearerInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(bearerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @NoAuth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientNoAuth(TopAdsAuthInterceptor tkpdBearerWithAuthInterceptor,
                                                  FingerprintInterceptor fingerprintInterceptor,
                                                  TkpdBaseInterceptor tkpdBaseInterceptor,
                                                  OkHttpRetryPolicy okHttpRetryPolicy,
                                                  ChuckerInterceptor chuckInterceptor,
                                                  DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientNoAuthWithBearer(tkpdBearerWithAuthInterceptor,
                fingerprintInterceptor,
                tkpdBaseInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);
    }

    @DefaultAuth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientDefaultAuth(FingerprintInterceptor fingerprintInterceptor,
                                                      TkpdAuthInterceptor tkpdAuthInterceptor,
                                                  OkHttpRetryPolicy okHttpRetryPolicy,
                                                  ChuckerInterceptor chuckInterceptor,
                                                  DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientDefaultAuth(fingerprintInterceptor,
                tkpdAuthInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);
    }

    @DefaultAuthWithErrorHandler
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientDefaultAuthWithErrorHandler(FingerprintInterceptor fingerprintInterceptor,
                                                                       TkpdAuthInterceptor tkpdAuthInterceptor,
                                                                       OkHttpRetryPolicy okHttpRetryPolicy,
                                                                       ChuckerInterceptor chuckInterceptor,
                                                                       DebugInterceptor debugInterceptor,
                                                                       TkpdErrorResponseInterceptor errorHandlerInterceptor){
        return OkHttpFactory.create().buildDaggerClientDefaultAuthWithErrorHandler(fingerprintInterceptor,
                tkpdAuthInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor,
                errorHandlerInterceptor);
    }

    @BearerAuth
    @ApplicationScope
    @Provides
    public OkHttpClient provideClientBearerAuth(FingerprintInterceptor fingerprintInterceptor,
                                                    StandardizedInterceptor standardizedInterceptor,
                                                    OkHttpRetryPolicy okHttpRetryPolicy,
                                                    ChuckerInterceptor chuckInterceptor,
                                                    DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientBearerAuth(fingerprintInterceptor,
                standardizedInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);

    }

    @MojitoAuth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientMojitoAuth(FingerprintInterceptor fingerprintInterceptor,
                                                      @Named(AuthUtil.KEY.KEY_MOJITO) GlobalTkpdAuthInterceptor globalTkpdAuthInterceptor,
                                                       OkHttpRetryPolicy okHttpRetryPolicy,
                                                       ChuckerInterceptor chuckInterceptor,
                                                       DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientAuth(fingerprintInterceptor,
                globalTkpdAuthInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);
    }

    @MojitoNoRetryAuth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientMojitoNoRetryAuth(FingerprintInterceptor fingerprintInterceptor,
                                                             @Named(AuthUtil.KEY.KEY_MOJITO) GlobalTkpdAuthInterceptor globalTkpdAuthInterceptor,
                                                             OkHttpRetryPolicy okHttpRetryPolicy,
                                                             ChuckerInterceptor chuckInterceptor,
                                                             DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientAuth(fingerprintInterceptor,
                globalTkpdAuthInterceptor,
                OkHttpRetryPolicy.createdOkHttpRetryPolicyQuickNoRetry(),
                chuckInterceptor,
                debugInterceptor);
    }

    @MojitoSmallTimeoutNoAuth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientMojitoSmallTimeoutNoAuth(FingerprintInterceptor fingerprintInterceptor,
                                                                    @Named(AuthUtil.KEY.KEY_MOJITO) GlobalTkpdAuthInterceptor globalTkpdAuthInterceptor,
                                                                    ChuckerInterceptor chuckInterceptor,
                                                                    DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientNoAuth(
                fingerprintInterceptor,
                globalTkpdAuthInterceptor,
                OkHttpRetryPolicy.createdOkHttpRetryPolicyQuickTimeOut(),
                chuckInterceptor,
                debugInterceptor
        );
    }

    @ApplicationScope
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

    @NoAuthNoFingerprint
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientNoAuthNoFingerprint(TkpdBaseInterceptor tkpdBaseInterceptor,
                                                  OkHttpRetryPolicy okHttpRetryPolicy,
                                                  ChuckerInterceptor chuckInterceptor,
                                                  DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientNoAuthNoFingerPrint(tkpdBaseInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);
    }

    @ApplicationScope
    @Provides
    public TkpdBearerWithAuthTypeJsonUtInterceptor provideTkpdBearerWithAuthTypeJsonUtInterceptor(){
        return new TkpdBearerWithAuthTypeJsonUtInterceptor();
    }

    @BearerAuthTypeJsonUt
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientWithAuthTypeJsonUt(TkpdBearerWithAuthTypeJsonUtInterceptor tkpdBearerWithAuthTypeJsonUtInterceptor,
                                                              OkHttpRetryPolicy okHttpRetryPolicy,
                                                              ChuckerInterceptor chuckInterceptor,
                                                              DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientBearerWithClientDefaultAuth(tkpdBearerWithAuthTypeJsonUtInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);
    }

    @UploadWsV4Auth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientUploadWsV4Auth(FingerprintInterceptor fingerprintInterceptor,
                                                          TkpdAuthInterceptor resolutionInterceptor,
                                                          OkHttpRetryPolicy okHttpRetryPolicy,
                                                          ChuckerInterceptor chuckInterceptor,
                                                          DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientResolutionAuth(fingerprintInterceptor,
                resolutionInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);
    }

    @TopAdsQualifier
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientTopAdsAuth(FingerprintInterceptor fingerprintInterceptor,
                                                      TopAdsAuthInterceptor topAdsAuthInterceptor,
                                                      OkHttpRetryPolicy okHttpRetryPolicy,
                                                      @TopAdsQualifier TkpdErrorResponseInterceptor errorResponseInterceptor) {

        return OkHttpFactory.create().buildDaggerClientBearerTopAdsAuth(fingerprintInterceptor,
                topAdsAuthInterceptor,
                okHttpRetryPolicy,
                errorResponseInterceptor);
    }
}
