package com.tokopedia.loginphone.common.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.loginphone.common.data.LoginRegisterPhoneApi;
import com.tokopedia.loginphone.common.data.LoginRegisterPhoneUrl;
import com.tokopedia.loginphone.common.network.TokoCashErrorResponse;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
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
public class LoginRegisterPhoneModule {

    @LoginRegisterPhoneScope
    @Provides
    LoginPhoneNumberAnalytics provideLoginAnalytics() {
        return new LoginPhoneNumberAnalytics();
    }

    @LoginRegisterPhoneScope
    @Provides
    OkHttpClient provideOkHttpClient(TkpdOldAuthInterceptor tkpdAuthInterceptor,
                                     ChuckInterceptor chuckInterceptor,
                                     DebugInterceptor debugInterceptor,
                                     HttpLoggingInterceptor httpLoggingInterceptor,
                                     FingerprintInterceptor fingerprintInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(fingerprintInterceptor);
        builder.addInterceptor(tkpdAuthInterceptor);
        builder.addInterceptor(new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class));
        builder.addInterceptor(new ErrorResponseInterceptor(TokoCashErrorResponse.class));

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor);
            builder.addInterceptor(debugInterceptor);
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return builder.build();
    }

    @LoginRegisterPhoneScope
    @Provides
    Retrofit provideLoginRegisterPhoneRetrofit(Retrofit.Builder retrofitBuilder,
                                               @LoginRegisterPhoneScope OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(LoginRegisterPhoneUrl.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @LoginRegisterPhoneScope
    @Provides
    LoginRegisterPhoneApi provideLoginRegisterPhoneApi(@LoginRegisterPhoneScope Retrofit retrofit) {
        return retrofit.create(LoginRegisterPhoneApi.class);
    }

    @LoginRegisterPhoneScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
