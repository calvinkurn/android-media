package com.tokopedia.sessioncommon.di;

import android.content.Context;
import android.content.res.Resources;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.sessioncommon.data.SessionCommonUrl;
import com.tokopedia.sessioncommon.data.TokenApi;
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference;
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreferenceManager;
import com.tokopedia.sessioncommon.network.AccountsBearerInterceptor;
import com.tokopedia.sessioncommon.network.BasicInterceptor;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
import com.tokopedia.sessioncommon.network.TokenErrorResponse;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 10/16/18.
 */
@Module
public class SessionModule {

    public static final String TOKEN = "TOKEN";

    public static final String SESSION_MODULE = "Session";

    @SessionCommonScope
    @Provides
    Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }

    @Named(SESSION_MODULE)
    @SessionCommonScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @SessionCommonScope
    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @SessionCommonScope
    @Provides
    TkpdOldAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                      NetworkRouter networkRouter,
                                                      @Named(SESSION_MODULE) UserSessionInterface userSession) {
        return new TkpdOldAuthInterceptor(context, networkRouter, userSession);
    }

    @SessionCommonScope
    @Provides
    ChuckerInterceptor provideChuckerInterceptor(@ApplicationContext Context context) {
        return new ChuckerInterceptor(context);
    }

    @SessionCommonScope
    @Provides
    FingerprintInterceptor provideFingerprintInterceptor(@Named(SESSION_MODULE) UserSessionInterface userSessionInterface,
                                                         NetworkRouter networkRouter) {
        return new FingerprintInterceptor(networkRouter, userSessionInterface);
    }

    @SessionCommonScope
    @Provides
    BasicInterceptor provideBasicInterceptor(@ApplicationContext Context context,
                                             @Named(SESSION_MODULE) UserSessionInterface userSessionInterface,
                                             NetworkRouter networkRouter) {
        return new BasicInterceptor(context, networkRouter, userSessionInterface);
    }

    @SessionCommonScope
    @Provides
    AccountsBearerInterceptor provideAccountsBearerInterceptor(@Named(SESSION_MODULE) UserSessionInterface userSessionInterface) {
        return new AccountsBearerInterceptor(userSessionInterface);
    }

    @SessionCommonScope
    @Provides
    @Named(TOKEN)
    OkHttpClient provideTokenOkHttpClient(@ApplicationContext Context context,
                                          BasicInterceptor basicInterceptor,
                                          ChuckerInterceptor chuckInterceptor,
                                          DebugInterceptor debugInterceptor,
                                          HttpLoggingInterceptor httpLoggingInterceptor,
                                          FingerprintInterceptor fingerprintInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(fingerprintInterceptor);
        builder.addInterceptor(basicInterceptor);
        builder.addInterceptor(new AkamaiBotInterceptor(context));
        builder.addInterceptor(new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class));
        builder.addInterceptor(new ErrorResponseInterceptor(TokenErrorResponse.class));
        builder.addInterceptor(chain -> {
            Request.Builder newRequest = chain.request().newBuilder();
            newRequest.addHeader("User-Agent", getUserAgent());
            return chain.proceed(newRequest.build());
        });

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor);
            builder.addInterceptor(debugInterceptor);
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return builder.build();
    }

    @SessionCommonScope
    @Provides
    @Named(TOKEN)
    Retrofit provideTokenRetrofit(Retrofit.Builder retrofitBuilder,
                                  @Named(TOKEN) OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(SessionCommonUrl.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @SessionCommonScope
    @Provides
    TokenApi provideTokenApi(@Named(TOKEN) Retrofit retrofit) {
        return retrofit.create(TokenApi.class);
    }

    @SessionCommonScope
    @Provides
    FingerprintPreference provideFingerprintPreferenceManager(@ApplicationContext Context context) {
        return new FingerprintPreferenceManager(context);
    }

    public static String getUserAgent() {
        return AuthHelper.getUserAgent();
    }
}
