package com.tokopedia.sessioncommon.di;

import android.content.Context;

import com.example.akamai_bot_lib.interceptor.AkamaiBotInterceptor;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.sessioncommon.data.GetProfileApi;
import com.tokopedia.sessioncommon.data.MakeLoginApi;
import com.tokopedia.sessioncommon.data.SessionCommonUrl;
import com.tokopedia.sessioncommon.data.TokenApi;
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
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 10/16/18.
 */
@Module
public class SessionModule {

    public static final String TOKEN = "TOKEN";
    public static final String WS = "WS";
    public static final String PROFILE = "PROFILE";
    private static final String SESSION_COMMON = "Session";

    public static final String SESSION_MODULE = "Session";

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
    ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context);
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
    @Named(SESSION_COMMON)
    OkHttpClient provideProfileOkHttpClient(TkpdOldAuthInterceptor tkpdAuthInterceptor,
                                            AccountsBearerInterceptor accountsBearerInterceptor,
                                            ChuckInterceptor chuckInterceptor,
                                            DebugInterceptor debugInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            FingerprintInterceptor fingerprintInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(fingerprintInterceptor);
        builder.addInterceptor(tkpdAuthInterceptor);
        builder.addInterceptor(new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class));
        builder.addInterceptor(accountsBearerInterceptor);
        builder.addInterceptor(new ErrorResponseInterceptor(TkpdV4ResponseError.class));

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(debugInterceptor);
            builder.addInterceptor(httpLoggingInterceptor);
            builder.addInterceptor(chuckInterceptor);
        }

        return builder.build();
    }

    @SessionCommonScope
    @Provides
    @Named(TOKEN)
    OkHttpClient provideTokenOkHttpClient(BasicInterceptor basicInterceptor,
                                          ChuckInterceptor chuckInterceptor,
                                          DebugInterceptor debugInterceptor,
                                          HttpLoggingInterceptor httpLoggingInterceptor,
                                          FingerprintInterceptor fingerprintInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(fingerprintInterceptor);
        builder.addInterceptor(basicInterceptor);
        builder.addInterceptor(new AkamaiBotInterceptor());
        builder.addInterceptor(new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class));
        builder.addInterceptor(new ErrorResponseInterceptor(TokenErrorResponse.class));

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor);
            builder.addInterceptor(debugInterceptor);
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return builder.build();
    }

    @SessionCommonScope
    @Provides
    @Named(PROFILE)
    Retrofit provideGetProfileRetrofit(Retrofit.Builder retrofitBuilder,
                                       @Named(SESSION_COMMON)
                                               OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(SessionCommonUrl.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @SessionCommonScope
    @Provides
    @Named(WS)
    Retrofit provideMakeLoginRetrofit(Retrofit.Builder retrofitBuilder,
                                      @Named(SESSION_COMMON)
                                              OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(SessionCommonUrl.BASE_WS_DOMAIN)
                .client(okHttpClient)
                .build();
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
    MakeLoginApi provideMakeLoginApi(@Named(WS) Retrofit retrofit) {
        return retrofit.create(MakeLoginApi.class);
    }

    @SessionCommonScope
    @Provides
    GetProfileApi provideGetProfileApi(@Named(PROFILE) Retrofit retrofit) {
        return retrofit.create(GetProfileApi.class);
    }

    @SessionCommonScope
    @Provides
    TokenApi provideTokenApi(@Named(TOKEN) Retrofit retrofit) {
        return retrofit.create(TokenApi.class);
    }
}
