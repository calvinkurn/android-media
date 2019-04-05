package com.tokopedia.loginphone.common.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.loginphone.common.data.LoginRegisterPhoneApi;
import com.tokopedia.sessioncommon.data.GetProfileApi;
import com.tokopedia.sessioncommon.data.MakeLoginApi;
import com.tokopedia.sessioncommon.data.TokenApi;
import com.tokopedia.sessioncommon.di.SessionCommonScope;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 10/15/18.
 */
@LoginRegisterPhoneScope
@SessionCommonScope
@Component(modules = {LoginRegisterPhoneModule.class, SessionModule.class},
        dependencies = BaseAppComponent.class)
public interface LoginRegisterPhoneComponent {

    LoginRegisterPhoneApi provideLoginRegisterApi();

    MakeLoginApi provideMakeLoginApi();

    GetProfileApi provideGetProfileApi();

    TokenApi provideTokenApi();

    @Named(SessionModule.SESSION_MODULE)
    UserSessionInterface provUserSessionInterface();

    LoginPhoneNumberAnalytics provideLoginPhoneNumberAnalytics();

    @ApplicationContext
    Context getContext();

    Retrofit.Builder retrofitBuilder();

    HttpLoggingInterceptor httpLoggingInterceptor();

    AbstractionRouter provideAbstractionRouter();

    UserSessionInterface userSession();

    TkpdAuthInterceptor tkpdAuthInterceptor();
}