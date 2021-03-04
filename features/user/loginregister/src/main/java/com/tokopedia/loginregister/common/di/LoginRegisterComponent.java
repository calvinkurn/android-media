package com.tokopedia.loginregister.common.di;

import android.content.Context;
import android.content.res.Resources;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics;
import com.tokopedia.loginregister.common.analytics.SeamlessLoginAnalytics;
import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.sessioncommon.data.TokenApi;
import com.tokopedia.sessioncommon.di.SessionCommonScope;
import com.tokopedia.sessioncommon.di.SessionModule;
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
@Component(modules = {
        LoginRegisterModule.class,
        SessionModule.class
}, dependencies = {BaseAppComponent.class})
public interface LoginRegisterComponent {

    @ApplicationContext
    Context getContext();

    Retrofit.Builder getRetrofitBuilder();

    CacheManager provideCacheManager();

    LoginRegisterApi provideLoginRegisterApi();

    @Named(SessionModule.SESSION_MODULE)
    UserSessionInterface provideUserSession();

    @Named(SessionModule.TOKEN)
    OkHttpClient provideTokenOkHttpClient();

    @Named(SessionModule.TOKEN)
    Retrofit provideTokenRetrofit();

    TokenApi provideTokenApi();

    LoginRegisterAnalytics provideLoginRegisterAnalytics();

    RegisterAnalytics provideRegisterAnalytics();

    Resources provideResources();

    PermissionCheckerHelper providePermissionCheckerHelper();

    SeamlessLoginAnalytics provideSeamlessLoginAnalytics();
}