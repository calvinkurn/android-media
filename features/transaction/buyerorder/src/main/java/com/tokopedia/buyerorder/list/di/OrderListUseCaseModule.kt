package com.tokopedia.buyerorder.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.di.OrderListDetailModule
import com.tokopedia.buyerorder.detail.domain.FinishOrderUseCase
import com.tokopedia.buyerorder.detail.domain.PostCancelReasonUseCase
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.buyerorder.list.view.presenter.OrderListPresenterImpl
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [TopAdsWishlistModule::class, OrderListDetailModule::class])
class OrderListUseCaseModule {

    @Provides
    @OrderListModuleScope
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @OrderListModuleScope
    fun provideUserSessionInterface(context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @OrderListModuleScope
    fun providesGraphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    @OrderListModuleScope
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_recommendation_widget)
    }

    @Provides
    @OrderListModuleScope
    fun provideGetRecommendationUseCase(@Named("recommendationQuery") recomQuery: String,
                                        graphqlUseCase: GraphqlUseCase,
                                        userSessionInterface: UserSessionInterface): GetRecommendationUseCase {
        return GetRecommendationUseCase(recomQuery, graphqlUseCase, userSessionInterface)
    }

    @Provides
    @OrderListModuleScope
    @Named("atcMutation")
    fun provideAddToCartMutation(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart)
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
                                       orderListAnalytics: OrderListAnalytics, postCancelReasonUseCase: PostCancelReasonUseCase,
                                       finishOrderUseCase: FinishOrderUseCase): OrderListPresenterImpl {
        return OrderListPresenterImpl(getRecommendationUseCase, addToCartUseCase, addWishListUseCase, removeWishListUseCase, topAdsWishlishedUseCase,
                userSessionInterface, orderListAnalytics, postCancelReasonUseCase, finishOrderUseCase)
    }

}