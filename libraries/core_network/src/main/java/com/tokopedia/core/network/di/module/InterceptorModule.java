package com.tokopedia.core.network.di.module;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerCollector;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.chuckerteam.chucker.api.RetentionManager;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.constant.ConstantCoreNetwork;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.network.di.qualifier.KeyDefaultQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.CreditCardInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TopAdsAuthInterceptor;
import com.tokopedia.core.network.retrofit.response.TkpdV4ResponseError;
import com.tokopedia.core.network.retrofit.response.TopAdsResponseError;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.user.session.UserSession;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 3/22/17.
 */

@Deprecated
@Module
public class InterceptorModule {

    @ApplicationScope
    @Provides
    public CacheApiInterceptor provideApiCacheInterceptor(@ApplicationContext Context context) {
        return new CacheApiInterceptor(context);
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
    public BearerInterceptor provideBearerInterceptor() {
        return new BearerInterceptor();
    }

    @ApplicationScope
    @Provides
    public StandardizedInterceptor provideStandardizedInterceptor() {
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String oAuthString = "Bearer " + userSession.getAccessToken();
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
    public FingerprintInterceptor provideFingerprintInterceptor(@ApplicationContext Context context) {
        return new FingerprintInterceptor(context);
    }

    @ApplicationScope
    @Provides
    public ChuckerInterceptor provideChuckerInterceptor(@ApplicationContext Context context,
                                                                  @Named(ConstantCoreNetwork.CHUCK_ENABLED) LocalCacheHandler localCacheHandler) {
        ChuckerCollector collector = new ChuckerCollector(
                context,
                localCacheHandler.getBoolean(ConstantCoreNetwork.IS_CHUCK_ENABLED, false),
                RetentionManager.Period.ONE_HOUR);

        return new ChuckerInterceptor(
                context, collector, 120000L);
    }

    @ApplicationScope
    @Provides
    public DebugInterceptor provideDebugInterceptor() {
        return new DebugInterceptor();
    }

    @Named(ConstantCoreNetwork.CHUCK_ENABLED)
    @ApplicationScope
    @Provides
    public LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, ConstantCoreNetwork.CHUCK_ENABLED);
    }

    @ApplicationScope
    @Provides
    TkpdErrorResponseInterceptor provideTkpdErrorResponseInterceptor() {
        return new TkpdErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @ApplicationScope
    @Provides
    public TopAdsAuthInterceptor provideTopAdsAuthInterceptor() {
        return new TopAdsAuthInterceptor();
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
