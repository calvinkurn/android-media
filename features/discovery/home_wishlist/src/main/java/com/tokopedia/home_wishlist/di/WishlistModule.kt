package com.tokopedia.home_wishlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartExecutors
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @WishlistScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    @WishlistScope
    fun provideGetRecommendationUseCase(@Named("recommendationQuery") recomQuery: String,
                                        graphqlUseCase: GraphqlUseCase,
                                        userSessionInterface: UserSessionInterface): GetRecommendationUseCase = GetRecommendationUseCase(recomQuery, graphqlUseCase, userSessionInterface)
    @Provides
    @WishlistScope
    fun provideGetSingleRecommendationUseCase(@Named("singleProductRecommendation") recomQuery: String,
                                        graphqlUseCase: GraphqlUseCase,
                                        userSessionInterface: UserSessionInterface): GetSingleRecommendationUseCase = GetSingleRecommendationUseCase(recomQuery, graphqlUseCase, userSessionInterface)

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
                    R.raw.query_recommendation_widget)

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