package com.tokopedia.loginregister.common.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.loginregister.common.data.LoginRegisterUrl;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.otp.common.network.WSErrorResponse;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 10/15/18.
 */
@Module
public class LoginRegisterModule {

    @LoginRegisterScope
    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @LoginRegisterScope
    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @LoginRegisterScope
    @Provides
    TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                   NetworkRouter networkRouter,
                                                   UserSessionInterface userSession) {
        return new TkpdAuthInterceptor(context, networkRouter, userSession);
    }

    @LoginRegisterScope
    @Provides
    ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context);
    }

    @LoginRegisterScope
    @Provides
    FingerprintInterceptor provideFingerprintInterceptor(UserSessionInterface userSessionInterface,
                                                         NetworkRouter networkRouter) {
        return new FingerprintInterceptor(networkRouter, userSessionInterface);
    }

    @LoginRegisterScope
    @Provides
    OkHttpClient provideOkHttpClient(TkpdAuthInterceptor tkpdAuthInterceptor,
                                     ChuckInterceptor chuckInterceptor,
                                     DebugInterceptor debugInterceptor,
                                     HttpLoggingInterceptor httpLoggingInterceptor,
                                     FingerprintInterceptor fingerprintInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor);
            builder.addInterceptor(debugInterceptor);
            builder.addInterceptor(httpLoggingInterceptor);
        }

        builder.addInterceptor(fingerprintInterceptor);
        builder.addInterceptor(tkpdAuthInterceptor);
        builder.addInterceptor(new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class));
        builder.addInterceptor(new ErrorResponseInterceptor(WSErrorResponse.class));

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

}
