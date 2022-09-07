package com.tokopedia.topads.sdk.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase;
import com.tokopedia.topads.sdk.repository.TopAdsRepository;
import com.tokopedia.topads.sdk.utils.CacheHandler;
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession;
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
    AddWishListUseCase addWishListUseCase(@ApplicationContext Context context){
        return new AddWishListUseCase(context);
    }

    @TopAdsScope
    @Provides
    RemoveWishListUseCase removeWishListUseCase(@ApplicationContext Context context){
        return new RemoveWishListUseCase(context);
    }

    @TopAdsScope
    @Provides
    TopAdsImageViewUseCase topAdsImageViewUseCase(@ApplicationContext Context context, UserSessionInterface userSession, TopAdsIrisSession topAdsIrisSession){
        return new TopAdsImageViewUseCase(userSession.getUserId(), new TopAdsRepository(), topAdsIrisSession.getSessionId());
    }
}
