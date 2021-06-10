package com.tokopedia.buyerorder.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiLegacyUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.buyerorder.detail.di.OrderListDetailModule
import com.tokopedia.buyerorder.detail.domain.FinishOrderGqlUseCase
import com.tokopedia.buyerorder.detail.domain.PostCancelReasonUseCase
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.buyerorder.list.view.presenter.OrderListPresenterImpl
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [TopAdsWishlistModule::class, RecommendationModule::class, OrderListDetailModule::class])
class OrderListUseCaseModule {

    @Provides
    @OrderListModuleScope
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @OrderListModuleScope
    fun providesGraphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    @OrderListModuleScope
    @Named("atcMutation")
    fun provideAddToCartMutation(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
    }

    @Provides
    @OrderListModuleScope
    fun provideAddWishlistUseCase(@ApplicationContext context: Context): AddWishListUseCase = AddWishListUseCase(context)

    @Provides
    @OrderListModuleScope
    fun provideRemoveWishlistUseCase(@ApplicationContext context: Context): RemoveWishListUseCase = RemoveWishListUseCase(context)

    @Provides
    @OrderListModuleScope
    fun providesOrderListPresenterImpl(getRecommendationUseCase: GetRecommendationUseCase, addToCartUseCase: AddToCartUseCase,
                                       addWishListUseCase: AddWishListUseCase, removeWishListUseCase: RemoveWishListUseCase,
                                       topAdsWishlishedUseCase: TopAdsWishlishedUseCase, userSessionInterface: UserSessionInterface,
                                       postCancelReasonUseCase: PostCancelReasonUseCase, addToCartMultiLegacyUseCase: AddToCartMultiLegacyUseCase,
                                       finishOrderGqlUseCase: FinishOrderGqlUseCase): OrderListPresenterImpl {
        return OrderListPresenterImpl(getRecommendationUseCase, addToCartUseCase, addWishListUseCase, removeWishListUseCase, topAdsWishlishedUseCase,
                userSessionInterface, postCancelReasonUseCase, addToCartMultiLegacyUseCase, finishOrderGqlUseCase)
    }

}