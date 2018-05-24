package com.tokopedia.posapp.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.di.module.PosApiModule;
import com.tokopedia.posapp.di.scope.PosApplicationScope;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author okasurya on 4/20/18.
 */
@PosApplicationScope
@Component(modules = PosApiModule.class, dependencies = BaseAppComponent.class)
public interface PosAppComponent {
    @ApplicationContext
    Context getContext();

    Retrofit.Builder retrofitBuilder();

    Gson gson();

    UserSession userSession();

    AbstractionRouter provideAbstractionRouter();

    TkpdAuthInterceptor tkpdAuthInterceptor();

    HeaderErrorResponseInterceptor headerErrorResponseInterceptor();

    HttpLoggingInterceptor httpLoggingInterceptor();

    CacheManager globalCacheManager();

    Retrofit providePosAuthRetrofit();

    PosSessionHandler providePosSessionHandler();
}
