package com.tokopedia.loginregister.common.di;

import android.content.Context;
import android.content.res.Resources;

import android.content.res.Resources;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics;
import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.sessioncommon.data.GetProfileApi;
import com.tokopedia.sessioncommon.data.MakeLoginApi;
import com.tokopedia.sessioncommon.data.TokenApi;
import com.tokopedia.sessioncommon.di.SessionCommonScope;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 10/15/18.
 */
@LoginRegisterScope
@SessionCommonScope
@Component(modules = {LoginRegisterModule.class, SessionModule.class}, dependencies = {BaseAppComponent.class})
public interface LoginRegisterComponent {

    @ApplicationContext
    Context getContext();

    Retrofit.Builder getRetrofitBuilder();

    CacheManager globalCacheManager();

    LoginRegisterApi provideLoginRegisterApi();

    @Named(SessionModule.SESSION_MODULE)
    UserSessionInterface provideUserSession();

    TkpdOldAuthInterceptor provideTkpdAuthInterceptor();

    @Named(SessionModule.TOKEN)
    OkHttpClient provideTokenOkHttpClient();

    @Named(SessionModule.PROFILE)
    Retrofit provideGetProfileRetrofit();

    @Named(SessionModule.WS)
    Retrofit provideMakeLoginRetrofit();

    @Named(SessionModule.TOKEN)
    Retrofit provideTokenRetrofit();

    MakeLoginApi provideMakeLoginApi();

    GetProfileApi provideGetProfileApi();

    TokenApi provideTokenApi();

    LoginRegisterAnalytics provideLoginRegisterAnalytics();

    RegisterAnalytics provideRegisterAnalytics();

    Resources provideResources();

}