package com.tokopedia.shop.favourite.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.feedcomponent.di.CoroutineDispatcherModule;
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase;
import com.tokopedia.shop.favourite.data.query.GetShopFollowerListQueryProvider;
import com.tokopedia.shop.favourite.di.scope.ShopFavouriteScope;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.shop.favourite.domain.interactor.GetShopFollowerListUseCase.QUERY_SHOP_FOLLOWER_LIST;

@ShopFavouriteScope
@Module(includes = {CoroutineDispatcherModule.class})
public class ShopFavouriteModule {

    @ShopFavouriteScope
    @Provides
    public DeleteShopInfoCacheUseCase provideDeleteShopInfoUseCase(@ApplicationContext Context context) {
        return new DeleteShopInfoCacheUseCase(context);
    }

    @ShopFavouriteScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ShopFavouriteScope
    @Provides
    @Named(QUERY_SHOP_FOLLOWER_LIST)
    String providesGetShopFollowerQuery() {
        return GetShopFollowerListQueryProvider.INSTANCE.getQuery();
    }
}

