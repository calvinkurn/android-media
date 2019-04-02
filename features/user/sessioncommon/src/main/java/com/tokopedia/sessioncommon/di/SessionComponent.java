package com.tokopedia.sessioncommon.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.sessioncommon.data.GetProfileApi;
import com.tokopedia.sessioncommon.data.MakeLoginApi;
import com.tokopedia.sessioncommon.data.TokenApi;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 10/16/18.
 */
@SessionCommonScope
@Component(modules = {SessionModule.class}, dependencies = {BaseAppComponent.class})
public interface SessionComponent {

    @ApplicationContext
    Context getContext();

    Retrofit.Builder getRetrofitBuilder();

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

    GetProfileApi provideLoginRegisterApi();

    TokenApi provideTokenApi();
}
