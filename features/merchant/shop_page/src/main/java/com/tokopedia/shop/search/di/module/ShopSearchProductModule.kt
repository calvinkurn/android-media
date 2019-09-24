package com.tokopedia.shop.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingShopSearchProduct
import com.tokopedia.shop.search.ShopSearchProductConstant.UNIVERSE_SEARCH_QUERY
import com.tokopedia.shop.search.di.scope.ShopSearchProductScope
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Named
import dagger.Module
import dagger.Provides


@ShopSearchProductScope
@Module(includes = [ShopSearchProductViewModelModule::class])
class ShopSearchProductModule {

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @ShopSearchProductScope
    @Provides
    fun provideUserSessionInterface(
            @ApplicationContext context: Context
    ): UserSessionInterface = UserSession(context)

    @ShopSearchProductScope
    @Provides
    @Named(UNIVERSE_SEARCH_QUERY)
    fun provideUniverseSearchQuery(
            @ApplicationContext context: Context
    ): String = GraphqlHelper.loadRawString(
            context.resources,
            R.raw.gql_universe_search
    )

    @ShopSearchProductScope
    @Provides
    fun provideShopPageTrackingShopSearchProduct(
            trackingQueue: TrackingQueue
    ) = ShopPageTrackingShopSearchProduct(trackingQueue)

    @ShopSearchProductScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)
}