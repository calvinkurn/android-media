package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.home.account.di.qualifier.AccountLogoutQualifier;
import com.tokopedia.home.account.di.scope.AccountLogoutScope;
import com.tokopedia.home.account.presentation.presenter.LogoutPresenter;
import com.tokopedia.logout.data.LogoutApi;
import com.tokopedia.logout.data.LogoutUrl;
import com.tokopedia.logout.domain.mapper.LogoutMapper;
import com.tokopedia.logout.domain.usecase.LogoutUseCase;
import com.tokopedia.navigation_common.model.WalletPref;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@AccountLogoutScope
@Module
public class LogoutModule {

    @AccountLogoutScope
    @Provides
    public FingerprintInterceptor provideFingerprintInterceptor(@ApplicationContext Context context,
                                                          UserSession userSession){
        return new FingerprintInterceptor((NetworkRouter)context.getApplicationContext(), userSession);
    }

    @AccountLogoutScope
    @Provides
    public TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                          com.tokopedia.user.session.UserSession userSession){
        return new TkpdAuthInterceptor(context, (NetworkRouter)context.getApplicationContext(), userSession);
    }

    @AccountLogoutScope
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context){
        return new UserSession(context);
    }

    @AccountLogoutScope
    @Provides
    public LogoutMapper provideLogoutMapper(){
        return new LogoutMapper();
    }

    @AccountLogoutQualifier
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

    @AccountLogoutQualifier
    @AccountLogoutScope
    @Provides
    public Retrofit provideRetrofit(@AccountLogoutQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(LogoutUrl.Companion.getBASE_URL())
                .client(okHttpClient).build();
    }

    @AccountLogoutScope
    @Provides
    public LogoutApi provideLogoutApi(@AccountLogoutQualifier Retrofit retrofit){
        return retrofit.create(LogoutApi.class);
    }

    @AccountLogoutScope
    @Provides
    public LogoutUseCase provideLogoutUseCase(LogoutApi logoutApi, LogoutMapper logoutMapper, UserSession userSession){
        return new LogoutUseCase(logoutApi, logoutMapper, userSession);
    }

    @AccountLogoutScope
    @Provides
    public LogoutPresenter provideDialogLogoutPresenter(LogoutUseCase logoutUseCase,
                                                        UserSession userSession, WalletPref walletPref){
        return new LogoutPresenter(logoutUseCase, userSession, walletPref);
    }

    @AccountLogoutScope
    @Provides
    public WalletPref provideWalletPref(@ApplicationContext Context context, Gson gson){
        return new WalletPref(context, gson);
    }
}
