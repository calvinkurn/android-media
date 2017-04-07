package com.tokopedia.core.network.di.module;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.StandardizedInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ricoharisin on 3/22/17.
 */

@Module
public class InterceptorModule {

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
    public StandardizedInterceptor provideStandardizedInterceptor() {
        return new StandardizedInterceptor(SessionHandler.getAccessToken());
    }

    @Named(AuthUtil.KEY.KEY_WSV4)
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

}
