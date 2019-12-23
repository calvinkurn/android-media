package com.tokopedia.autocomplete.di.net;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.autocomplete.di.AutoCompleteScope;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;

import dagger.Module;
import dagger.Provides;

@AutoCompleteScope
@Module
public class AutoCompleteInterceptorModule {
    @AutoCompleteScope
    @Provides
    public DebugInterceptor provideDebugInterceptor() {
        return new DebugInterceptor();
    }

    @AutoCompleteScope
    @Provides
    public CacheApiInterceptor provideApiCacheInterceptor(@ApplicationContext Context context) {
        return new CacheApiInterceptor(context);
    }

    @AutoCompleteScope
    @Provides
    ChuckerInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckerInterceptor(context);
    }

    @AutoCompleteScope
    @Provides
    public TkpdBaseInterceptor provideTkpdBaseInterceptor() {
        return new TkpdBaseInterceptor();
    }
}
