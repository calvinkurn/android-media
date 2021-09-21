package com.tokopedia.shop.search.di.module

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.shop.analytic.ShopPageTrackingShopSearchProduct
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.search.di.scope.ShopSearchProductScope
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides


@Module(includes = [ShopSearchProductViewModelModule::class])
class ShopSearchProductModule {
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @ShopSearchProductScope
    @Provides
    fun provideUserSessionInterface(
            @ShopPageContext context: Context
    ): UserSessionInterface = UserSession(context)

    @ShopSearchProductScope
    @Provides
    fun provideNewShopPageTrackingShopSearchProduct(
            trackingQueue: TrackingQueue
    ) = ShopPageTrackingShopSearchProduct(trackingQueue)

    @ShopSearchProductScope
    @Provides
    fun provideTrackingQueue(@ShopPageContext context: Context) = TrackingQueue(context)
}