package com.tokopedia.home_wishlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartExecutors
import com.tokopedia.home_wishlist.common.WishlistDispatcherProvider
import com.tokopedia.home_wishlist.common.WishlistProductionDispatcherProvider
import com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.RecommendationDataSource
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.BulkRemoveWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * A class module for dagger recommendation page
 */
@Module(includes = [TopAdsWishlistModule::class])
class WishlistModule {
    @Provides
    fun provideExecutors() = SmartExecutors()

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @WishlistScope
    @Provides
    fun provideWishlistProductionDispatcherProvider(): WishlistDispatcherProvider = WishlistProductionDispatcherProvider()

    @Provides
    @WishlistScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    @WishlistScope
    fun provideGetRecommendationUseCase(@Named("recommendationQuery") recomQuery: String,
                                        @Named("singleProductRecommendation") singleRecomQuery: String,
                                        graphqlRepository: GraphqlRepository,
                                        userSessionInterface: UserSessionInterface): RecommendationDataSource = RecommendationDataSource(recomQuery, singleRecomQuery, graphqlRepository, userSessionInterface)
    @Provides
    @WishlistScope
    fun provideGetSingleRecommendationUseCase(@Named("singleProductRecommendation") recomQuery: String,
                                        graphqlUseCase: GraphqlUseCase,
                                        userSessionInterface: UserSessionInterface): GetSingleRecommendationUseCase = GetSingleRecommendationUseCase(recomQuery, graphqlUseCase, userSessionInterface)

    @Provides
    @WishlistScope
    fun provideAddWishlistUseCase(@ApplicationContext context: Context): AddWishListUseCase = AddWishListUseCase(context)

    @Provides
    @WishlistScope
    fun provideRemoveWishlistUseCase(@ApplicationContext context: Context): RemoveWishListUseCase = RemoveWishListUseCase(context)

    @Provides
    @WishlistScope
    fun provideBulkRemoveWishlistUseCase(@ApplicationContext context: Context) = BulkRemoveWishlistUseCase(context)

    @Provides
    @WishlistScope
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    R.raw.query_recommendation_widget)

    @Provides
    @WishlistScope
    @Named("singleProductRecommendation")
    fun provideSingleProductRecommendationRawQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    R.raw.query_single_recommendation_widget)

    @Provides
    @WishlistScope
    @Named("wishlistQuery")
    fun provideWishlistQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    R.raw.query_get_wishlist)

    @Provides
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    R.raw.mutation_add_to_cart)

}