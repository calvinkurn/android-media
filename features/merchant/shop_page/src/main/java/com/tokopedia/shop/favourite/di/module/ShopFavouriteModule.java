package com.tokopedia.shop.favourite.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.data.source.cloud.api.ShopWSApi;
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase;
import com.tokopedia.shop.favourite.data.repository.ShopFavouriteRepositoryImpl;
import com.tokopedia.shop.favourite.data.source.ShopFavouriteDataSource;
import com.tokopedia.shop.favourite.data.source.cloud.ShopFavouriteCloudDataSource;
import com.tokopedia.shop.favourite.di.scope.ShopFavouriteScope;
import com.tokopedia.shop.favourite.domain.repository.ShopFavouriteRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@ShopFavouriteScope
@Module
public class ShopFavouriteModule {

    @ShopFavouriteScope
    @Provides
    public DeleteShopInfoCacheUseCase provideDeleteShopInfoUseCase() {
        return new DeleteShopInfoCacheUseCase();
    }

    @ShopFavouriteScope
    @Provides
    public ShopFavouriteCloudDataSource provideShopFavouriteCloudDataSource(ShopWSApi shopWS4Api) {
        return new ShopFavouriteCloudDataSource(shopWS4Api);
    }

    @ShopFavouriteScope
    @Provides
    public ShopFavouriteDataSource provideShopFavouriteDataSource(ShopFavouriteCloudDataSource ShopFavouriteCloudDataSource) {
        return new ShopFavouriteDataSource(ShopFavouriteCloudDataSource);
    }

    @ShopFavouriteScope
    @Provides
    public ShopFavouriteRepository provideShopFavouriteRepository(ShopFavouriteDataSource ShopFavouriteDataSource) {
        return new ShopFavouriteRepositoryImpl(ShopFavouriteDataSource);
    }

    @ShopFavouriteScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}

