package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.home.account.data.util.NotifPreference;
import com.tokopedia.home.account.di.qualifier.AccountLogoutQualifier;
import com.tokopedia.home.account.di.scope.AccountLogoutScope;
import com.tokopedia.home.account.domain.SendNotifUseCase;
import com.tokopedia.home.account.presentation.presenter.RedDotGimmickPresenter;
import com.tokopedia.navigation_common.model.WalletPref;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

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

    @AccountLogoutScope
    @Provides
    public RedDotGimmickPresenter provideDialogLogoutPresenter(SendNotifUseCase sendNotifUseCase){
        return new RedDotGimmickPresenter(sendNotifUseCase);
    }

    @AccountLogoutScope
    @Provides
    public WalletPref provideWalletPref(@ApplicationContext Context context, Gson gson){
        return new WalletPref(context, gson);
    }

    @AccountLogoutScope
    @Provides
    public GraphqlRepository provideGraphQlRepository(){
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @AccountLogoutScope
    @Provides
    public NotifPreference provideNotifPreference(@ApplicationContext Context context){
        return new NotifPreference(context);
    }
}
