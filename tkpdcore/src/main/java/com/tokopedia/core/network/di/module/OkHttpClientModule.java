package com.tokopedia.core.network.di.module;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.cache.interceptor.ApiCacheInterceptor;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.di.qualifier.BearerAuth;
import com.tokopedia.core.network.di.qualifier.BearerAuthTypeJsonUt;
import com.tokopedia.core.network.di.qualifier.DefaultAuth;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.MojitoAuth;
import com.tokopedia.core.network.di.qualifier.NoAuth;
import com.tokopedia.core.network.di.qualifier.NoAuthNoFingerprint;
import com.tokopedia.core.network.di.qualifier.UploadWsV4Auth;
import com.tokopedia.core.network.di.qualifier.WsV4Auth;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.ResolutionInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBearerWithAuthTypeJsonUtInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author  ricoharisin on 3/23/17.
 */

@Module(includes={InterceptorModule.class})
public class OkHttpClientModule {

    @NoAuth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientNoAuth(FingerprintInterceptor fingerprintInterceptor,
                                                  TkpdBaseInterceptor tkpdBaseInterceptor,
                                                  OkHttpRetryPolicy okHttpRetryPolicy,
                                                  ChuckInterceptor chuckInterceptor,
                                                  DebugInterceptor debugInterceptor,
                                                  ApiCacheInterceptor apiCacheInterceptor) {

        return OkHttpFactory.create().buildDaggerClientNoAuth(fingerprintInterceptor,
                tkpdBaseInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor,
                apiCacheInterceptor);
    }

    @DefaultAuth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientDefaultAuth(FingerprintInterceptor fingerprintInterceptor,
                                                      TkpdAuthInterceptor tkpdAuthInterceptor,
                                                  OkHttpRetryPolicy okHttpRetryPolicy,
                                                  ChuckInterceptor chuckInterceptor,
                                                  DebugInterceptor debugInterceptor,
                                                       ApiCacheInterceptor apiCacheInterceptor) {

        return OkHttpFactory.create().buildDaggerClientDefaultAuth(fingerprintInterceptor,
                tkpdAuthInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor,
                apiCacheInterceptor);
    }

    @DefaultAuthWithErrorHandler
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientDefaultAuthWithErrorHandler(FingerprintInterceptor fingerprintInterceptor,
                                                                       TkpdAuthInterceptor tkpdAuthInterceptor,
                                                                       OkHttpRetryPolicy okHttpRetryPolicy,
                                                                       ChuckInterceptor chuckInterceptor,
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
                                                    ChuckInterceptor chuckInterceptor,
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
                                                       ChuckInterceptor chuckInterceptor,
                                                       DebugInterceptor debugInterceptor,
                                                      ApiCacheInterceptor apiCacheInterceptor) {

        return OkHttpFactory.create().buildDaggerClientAuth(fingerprintInterceptor,
                globalTkpdAuthInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor,
                apiCacheInterceptor);
    }

    @WsV4Auth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientWsV4Auth(FingerprintInterceptor fingerprintInterceptor,
                                                      @Named(AuthUtil.KEY.KEY_WSV4) GlobalTkpdAuthInterceptor globalTkpdAuthInterceptor,
                                                      OkHttpRetryPolicy okHttpRetryPolicy,
                                                      ChuckInterceptor chuckInterceptor,
                                                      DebugInterceptor debugInterceptor,
                                                    ApiCacheInterceptor apiCacheInterceptor) {

        return OkHttpFactory.create().buildDaggerClientAuth(fingerprintInterceptor,
                globalTkpdAuthInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor,
                apiCacheInterceptor);
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
                                                  ChuckInterceptor chuckInterceptor,
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
                                                              ChuckInterceptor chuckInterceptor,
                                                              DebugInterceptor debugInterceptor,
                                                              ApiCacheInterceptor apiCacheInterceptor) {
        return OkHttpFactory.create().buildDaggerClientBearerWithClientDefaultAuth(tkpdBearerWithAuthTypeJsonUtInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor,
                apiCacheInterceptor);
    }

    @UploadWsV4Auth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientUploadWsV4Auth(FingerprintInterceptor fingerprintInterceptor,
                                                          ResolutionInterceptor resolutionInterceptor,
                                                          OkHttpRetryPolicy okHttpRetryPolicy,
                                                          ChuckInterceptor chuckInterceptor,
                                                          DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientResolutionAuth(fingerprintInterceptor,
                resolutionInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);
    }
}
