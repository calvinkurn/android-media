package com.tokopedia.topads.sdk.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.topads.sdk.base.AuthInterceptor;
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsUseCase;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

@Module
public class TopAdsModule {

    @TopAdsScope
    @Provides
    public AuthInterceptor provideTopAdsAuthTempInterceptor(@ApplicationContext Context context,
                                                            NetworkRouter networkRouter,
                                                            UserSession userSession){
        return new AuthInterceptor(context, networkRouter, userSession);
    }

    @TopAdsScope
    @Provides
    UserSession provideUserSession(@ApplicationContext Context context){
        return new UserSession(context);
    }

    @TopAdsScope
    @Provides
    TopAdsPresenter provideTopAdsPresenter(@ApplicationContext Context context){
        return new TopAdsPresenter(context);
    }

    @TopAdsScope
    @Provides
    TopAdsUseCase provideTopAdsUseCase(@ApplicationContext Context context){
        return new TopAdsUseCase(context);
    }

    @TopAdsScope
    @Provides
    OpenTopAdsUseCase provideOpenTopAdsUseCase(@ApplicationContext Context context){
        return new OpenTopAdsUseCase(context);
    }
}
