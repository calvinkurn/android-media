package com.tokopedia.cart.view.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.cart.domain.usecase.FollowShopUseCase
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-08-23.
 */

@Module(includes = [
    RecommendationModule::class,
    PurchasePlatformBaseModule::class
])
class CartModule {

    @Provides
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @CartScope
    fun provideGetWishlistUseCase(@ApplicationContext context: Context): GetWishlistUseCase {
        return GetWishlistUseCase(context)
    }

    @Provides
    @CartScope
    fun providesAddWishListUseCase(@ApplicationContext context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @Provides
    @CartScope
    fun providesRemoveWishListUseCase(@ApplicationContext context: Context): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @Provides
    @CartScope
    fun provideCheckoutAnalyticsCart(@ApplicationContext context: Context): CheckoutAnalyticsCart {
        return CheckoutAnalyticsCart(context)
    }

    @Provides
    @CartScope
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    @CartScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @Provides
    @CartScope
    fun provideExecutorSchedulers(): ExecutorSchedulers = DefaultSchedulers

    @Provides
    @CartScope
    @Named(AtcConstant.MUTATION_UPDATE_CART_COUNTER)
    fun provideUpdateCartCounterMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.gql_update_cart_counter)
    }

    @Provides
    @CartScope
    @Named(AtcConstant.MUTATION_ATC_EXTERNAL)
    fun provideAddToCartExternalMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_external)
    }

    @Provides
    @CartScope
    @Named(FollowShopUseCase.MUTATION_NAME)
    fun provideFollowShopMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.shop.common.R.raw.gql_mutation_favorite_shop)
    }

}