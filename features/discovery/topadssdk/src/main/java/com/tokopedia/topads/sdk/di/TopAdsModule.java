package com.tokopedia.topads.sdk.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsGqlUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsUseCase;
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.utils.CacheHandler;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class TopAdsModule {

    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context){
        return new UserSession(context);
    }

    @TopAdsScope
    @Provides
    public TopAdsGqlUseCase provideTopAdsGqlUseCase(@ApplicationContext Context context){
        return new TopAdsGqlUseCase(context);
    }

    @TopAdsScope
    @Provides
    BannerAdsPresenter provideBannerAdsPresenter(@ApplicationContext Context context){
        return new BannerAdsPresenter(context);
    }

    @TopAdsScope
    @Provides
    TopAdsPresenter provideTopAdsPresenter(@ApplicationContext Context context){
        return new TopAdsPresenter(context);
    }

    @TopAdsScope
    @Provides
    TopAdsParams provideTopAdsParams(){
        return new TopAdsParams();
    }

    @TopAdsScope
    @Provides
    CacheHandler provideCacheHandler(@ApplicationContext Context context){
        return new CacheHandler(context, CacheHandler.TOP_ADS_CACHE);
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

    @TopAdsScope
    @Provides
    AddWishListUseCase addWishListUseCase(@ApplicationContext Context context){
        return new AddWishListUseCase(context);
    }

    @TopAdsScope
    @Provides
    RemoveWishListUseCase removeWishListUseCase(@ApplicationContext Context context){
        return new RemoveWishListUseCase(context);
    }
}
