package com.tokopedia.loginregister.common.di;

import android.content.Context;

import android.content.res.Resources;

import com.example.akamai_bot_lib.interceptor.AkamaiBotInterceptor;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics;
import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.loginregister.common.data.LoginRegisterUrl;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.RiskAnalyticsInterceptor;
import com.tokopedia.otp.common.network.WSErrorResponse;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import javax.inject.Named;

/**
 * @author by nisie on 10/15/18.
 */
@Module
public class LoginRegisterModule {

    @LoginRegisterScope
    @Provides
    LoginRegisterAnalytics provideLoginRegisterAnalytics(@Named(SessionModule.SESSION_MODULE) UserSessionInterface userSessionInterface) {
        return new LoginRegisterAnalytics(userSessionInterface);
    }

    @LoginRegisterScope
    @Provides
    RegisterAnalytics provideRegisterAnalytics() {
        return new RegisterAnalytics();
    }

    @LoginRegisterScope
    @Provides
    OkHttpClient provideOkHttpClient(@ApplicationContext Context context,
                                     TkpdOldAuthInterceptor tkpdAuthInterceptor,
                                     ChuckInterceptor chuckInterceptor,
                                     DebugInterceptor debugInterceptor,
                                     HttpLoggingInterceptor httpLoggingInterceptor,
                                     FingerprintInterceptor fingerprintInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(tkpdAuthInterceptor);
        builder.addInterceptor(fingerprintInterceptor);
        builder.addInterceptor(new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class));
        builder.addInterceptor(new ErrorResponseInterceptor(TkpdV4ResponseError.class));
        builder.addInterceptor(new RiskAnalyticsInterceptor(context));
        builder.addInterceptor(new AkamaiBotInterceptor());

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor);
            builder.addInterceptor(debugInterceptor);
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return builder.build();
    }

    @LoginRegisterScope
    @Provides
    Retrofit provideLoginRegisterRetrofit(Retrofit.Builder retrofitBuilder,
                                          @LoginRegisterScope OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(LoginRegisterUrl.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @LoginRegisterScope
    @Provides
    LoginRegisterApi provideLoginRegisterApi(@LoginRegisterScope Retrofit retrofit) {
        return retrofit.create(LoginRegisterApi.class);
    }

    @LoginRegisterScope
    @Provides
    PermissionCheckerHelper providePermissionCheckerHelper(){
        return new PermissionCheckerHelper();
    }
}
