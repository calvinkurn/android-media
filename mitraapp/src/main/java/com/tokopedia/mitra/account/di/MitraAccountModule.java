package com.tokopedia.mitra.account.di;


import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.logout.data.LogoutApi;
import com.tokopedia.logout.data.LogoutUrl;
import com.tokopedia.logout.domain.mapper.LogoutMapper;
import com.tokopedia.logout.domain.usecase.LogoutUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
public class MitraAccountModule {

    @MitraAccountScope
    @Provides
    public FingerprintInterceptor provideFingerprintInterceptor(NetworkRouter networkRouter,
                                                                UserSession userSession) {
        return new FingerprintInterceptor(networkRouter, userSession);
    }

    @MitraAccountScope
    @Provides
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        if (context instanceof NetworkRouter) {
            return (NetworkRouter) context;
        }
        throw new RuntimeException("Application must implement " + NetworkRouter.class.getSimpleName());
    }

    @MitraAccountScope
    @Provides
    public TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                          com.tokopedia.user.session.UserSession userSession) {
        return new TkpdAuthInterceptor(context, (NetworkRouter) context.getApplicationContext(), userSession);
    }

    @MitraAccountScope
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @MitraAccountScope
    @Provides
    public LogoutMapper provideLogoutMapper() {
        return new LogoutMapper();
    }

    @MitraAccountScope
    @Provides
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,
                                            FingerprintInterceptor fingerprintInterceptor,
                                            TkpdAuthInterceptor authInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(authInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @MitraAccountScope
    @Provides
    public Retrofit provideRetrofit(@MitraAccountScope OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(LogoutUrl.Companion.getBASE_URL())
                .client(okHttpClient).build();
    }

    @MitraAccountScope
    @Provides
    public LogoutApi provideLogoutApi(Retrofit retrofit) {
        return retrofit.create(LogoutApi.class);
    }

    @MitraAccountScope
    @Provides
    public LogoutUseCase provideLogoutUseCase(LogoutApi logoutApi, LogoutMapper logoutMapper, UserSession userSession) {
        return new LogoutUseCase(logoutApi, logoutMapper, userSession);
    }
}
