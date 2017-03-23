package com.tokopedia.core.network.di.module;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.di.qualifier.BearerAuth;
import com.tokopedia.core.network.di.qualifier.DefaultAuth;
import com.tokopedia.core.network.di.qualifier.GlobalAuth;
import com.tokopedia.core.network.di.qualifier.MojitoAuth;
import com.tokopedia.core.network.di.qualifier.NoAuth;
import com.tokopedia.core.network.di.qualifier.WsV4Auth;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
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
                                                  DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientNoAuth(fingerprintInterceptor,
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
                                                  ChuckInterceptor chuckInterceptor,
                                                  DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientDefaultAuth(fingerprintInterceptor,
                tkpdAuthInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);
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
                                                       DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientAuth(fingerprintInterceptor,
                globalTkpdAuthInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);
    }

    @WsV4Auth
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClientWsV4Auth(FingerprintInterceptor fingerprintInterceptor,
                                                      @Named(AuthUtil.KEY.KEY_WSV4) GlobalTkpdAuthInterceptor globalTkpdAuthInterceptor,
                                                      OkHttpRetryPolicy okHttpRetryPolicy,
                                                      ChuckInterceptor chuckInterceptor,
                                                      DebugInterceptor debugInterceptor) {

        return OkHttpFactory.create().buildDaggerClientAuth(fingerprintInterceptor,
                globalTkpdAuthInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor);
    }

    @ApplicationScope
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

}
