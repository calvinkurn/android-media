package com.tokopedia.core.network.di.module;

import android.content.Context;
import android.util.Log;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.cache.interceptor.ApiCacheInterceptor;
import com.tokopedia.core.network.di.qualifier.KeyDefaultQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.retrofit.interceptors.CreditCardInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.ResolutionInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TopAdsAuthInterceptor;
import com.tokopedia.core.network.retrofit.response.TkpdV4ResponseError;
import com.tokopedia.core.network.retrofit.response.TopAdsResponseError;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import javax.inject.Named;
import javax.inject.Qualifier;

import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 3/22/17.
 */

@Module
public class InterceptorModule {

    @ApplicationScope
    @Provides
    public ApiCacheInterceptor provideApiCacheInterceptor() {
        return new ApiCacheInterceptor();
    }

    @ApplicationScope
    @Provides
    public TkpdAuthInterceptor provideTkpdAuthInterceptor() {
        return new TkpdAuthInterceptor();
    }

    @ApplicationScope
    @Provides
    public TkpdBaseInterceptor provideTkpdBaseInterceptor() {
        return new TkpdBaseInterceptor();
    }

    @ApplicationScope
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @ApplicationScope
    @Provides
    public BearerInterceptor provideBearerInterceptor(SessionHandler sessionHandler) {
        return new BearerInterceptor(sessionHandler);
    }

    @ApplicationScope
    @Provides
    public StandardizedInterceptor provideStandardizedInterceptor() {
        String oAuthString = "Bearer " + SessionHandler.getAccessToken();
        return new StandardizedInterceptor(oAuthString);
    }

    @KeyDefaultQualifier
    @ApplicationScope
    @Provides
    public GlobalTkpdAuthInterceptor provideWsV4TkpdAuthInterceptor() {
        return new GlobalTkpdAuthInterceptor(AuthUtil.KEY.KEY_WSV4);
    }

    @Named(AuthUtil.KEY.KEY_MOJITO)
    @ApplicationScope
    @Provides
    public GlobalTkpdAuthInterceptor provideMojitoTkpdAuthInterceptor() {
        return new GlobalTkpdAuthInterceptor(AuthUtil.KEY.KEY_MOJITO);
    }

    @ApplicationScope
    @Provides
    public FingerprintInterceptor provideFingerprintInterceptor() {
        return new FingerprintInterceptor();
    }

    @ApplicationScope
    @Provides
    public ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context,
                                                    @Named(DeveloperOptions.CHUCK_ENABLED) LocalCacheHandler localCacheHandler) {
        return new ChuckInterceptor(context)
                .showNotification(localCacheHandler.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false));
    }

    @ApplicationScope
    @Provides
    public DebugInterceptor provideDebugInterceptor() {
        return new DebugInterceptor();
    }

    @Named(DeveloperOptions.CHUCK_ENABLED)
    @ApplicationScope
    @Provides
    public LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, DeveloperOptions.CHUCK_ENABLED);
    }

    @ApplicationScope
    @Provides
    TkpdErrorResponseInterceptor provideTkpdErrorResponseInterceptor() {
        return new TkpdErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @ApplicationScope
    @Provides
    public ResolutionInterceptor provideResolutionInterceptor() {
        return new ResolutionInterceptor();
    }

    @ApplicationScope
    @Provides
    public TopAdsAuthInterceptor provideTopAdsAuthInterceptor(
            SessionHandler sessionHandler) {
        return new TopAdsAuthInterceptor(sessionHandler);
    }

    @TopAdsQualifier
    @ApplicationScope
    @Provides
    TkpdErrorResponseInterceptor provideTopAdsErrorResponseInterceptor() {
        return new TkpdErrorResponseInterceptor(TopAdsResponseError.class);
    }

    @ApplicationScope
    @Provides
    public CreditCardInterceptor provideCreditCardInterceptor() {
        return new CreditCardInterceptor();
    }
}
