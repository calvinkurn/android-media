package com.tokopedia.logintest.common.di;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.sessioncommon.data.TokenApi;
import com.tokopedia.sessioncommon.di.SessionCommonScope;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.utils.permission.PermissionCheckerHelper;

import javax.inject.Named;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 10/15/18.
 */
@LoginRegisterTestAppScope
@SessionCommonScope
@Component(modules = {
        LoginRegisterTestAppModule.class,
        SessionModule.class
}, dependencies = {BaseAppComponent.class})
public interface LoginRegisterTestAppComponent {

    @ApplicationContext
    Context getContext();

    Retrofit.Builder getRetrofitBuilder();

    CacheManager provideCacheManager();

    @Named(SessionModule.SESSION_MODULE)
    UserSessionInterface provideUserSession();

    @Named(SessionModule.TOKEN)
    OkHttpClient provideTokenOkHttpClient();

    @Named(SessionModule.TOKEN)
    Retrofit provideTokenRetrofit();

    TokenApi provideTokenApi();

    Resources provideResources();

    PermissionCheckerHelper providePermissionCheckerHelper();

}