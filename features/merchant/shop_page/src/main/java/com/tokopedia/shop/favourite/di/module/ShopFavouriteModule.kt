package com.tokopedia.shop.favourite.di.module

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.favourite.data.query.GetShopFollowerListQueryProvider.getQuery
import com.tokopedia.shop.favourite.di.scope.ShopFavouriteScope
import com.tokopedia.shop.favourite.domain.interactor.GetShopFollowerListUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ShopFavouriteModule {
    @ShopFavouriteScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopFavouriteScope
    @Provides
    @Named(GetShopFollowerListUseCase.QUERY_SHOP_FOLLOWER_LIST)
    fun providesGetShopFollowerQuery(): String {
        return getQuery()
    }

    @ShopFavouriteScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }
}