package com.tokopedia.shop.home.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.home.GqlQueryConstant.GQL_ATC_MUTATION
import com.tokopedia.shop.home.GqlQueryConstant.GQL_GET_SHOP_PAGE_HOME_LAYOUT
import com.tokopedia.shop.home.HomeConstant
import com.tokopedia.shop.home.di.scope.ShopPageHomeScope
import com.tokopedia.shop.home.util.CoroutineDispatcherProviderImpl
import com.tokopedia.shop.home.util.CoroutineDispatcherProvider
import com.tokopedia.shop.product.data.GQLQueryConstant
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@ShopPageHomeScope
@Module(includes = [ShopPageHomeViewModelModule::class])
class ShopPageHomeModule {

    @ShopPageHomeScope
    @Provides
    @Named(GQL_GET_SHOP_PAGE_HOME_LAYOUT)
    fun getShopFeaturedProductQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_page_home_layout)
    }

    @ShopPageHomeScope
    @Provides
    @Named(GQLQueryConstant.SHOP_PRODUCT)
    fun getShopProductQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_product)
    }

    @ShopPageHomeScope
    @Provides
    @Named(GQL_ATC_MUTATION)
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart);
    }

    @ShopPageHomeScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProviderImpl
    }

    @ShopPageHomeScope
    @Provides
    fun getShopProductUseCase(
            @Named(GQLQueryConstant.SHOP_PRODUCT) gqlQuery: String,
            gqlUseCase: MultiRequestGraphqlUseCase
    ): GqlGetShopProductUseCase {
        return GqlGetShopProductUseCase(gqlQuery, gqlUseCase)
    }

    @ShopPageHomeScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopPageHomeScope
    @Provides
    fun provideShopPageHomeTracking(@ApplicationContext context: Context): ShopPageHomeTracking {
        return ShopPageHomeTracking(TrackingQueue(context))
    }

}