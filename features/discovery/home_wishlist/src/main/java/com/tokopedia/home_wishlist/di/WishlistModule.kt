package com.tokopedia.home_wishlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.domain.GetWishlistDataUseCase
import com.tokopedia.home_wishlist.domain.SendTopAdsUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
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
open class WishlistModule {
    @WishlistScope
    @Provides
    fun provideExecutors(): SmartExecutors = SmartExecutors()

    @WishlistScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @WishlistScope
    @Provides
    fun providesGraphqlUsecase(): GraphqlUseCase = GraphqlUseCase()

    @WishlistScope
    @Provides
    fun provideWishlistProductionDispatcherProvider(): CoroutineDispatchers = CoroutineDispatchersProvider

    @Provides
    @WishlistScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    @WishlistScope
    open fun provideGetWishlistDataUseCase(repository: WishlistRepository): GetWishlistDataUseCase = GetWishlistDataUseCase(repository)

    @Provides
    @WishlistScope
    fun provideWishlistRepository(graphqlRepository: GraphqlRepository): WishlistRepository = WishlistRepository(graphqlRepository)

    @Provides
    @WishlistScope
    fun provideAddWishlistUseCase(@ApplicationContext context: Context): AddWishListUseCase = AddWishListUseCase(context)

    @Provides
    @WishlistScope
    open fun provideGetSingleRecommendationUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetSingleRecommendationUseCase = GetSingleRecommendationUseCase(context, graphqlRepository)

    @Provides
    @WishlistScope
    fun provideGetRecommendationUseCase(@ApplicationContext context: Context, coroutineGqlRepository: GraphqlRepository): GetRecommendationUseCase = GetRecommendationUseCase(context, coroutineGqlRepository)

    @Provides
    @WishlistScope
    fun provideRemoveWishlistUseCase(@ApplicationContext context: Context): RemoveWishListUseCase = RemoveWishListUseCase(context)

    @Provides
    @WishlistScope
    fun provideBulkRemoveWishlistUseCase(graphqlUseCase: GraphqlUseCase): BulkRemoveWishlistUseCase = BulkRemoveWishlistUseCase(graphqlUseCase)

    @Provides
    @WishlistScope
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface): TopAdsImageViewUseCase = TopAdsImageViewUseCase(userSession.userId, TopAdsRepository())

    @Provides
    @WishlistScope
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    com.tokopedia.recommendation_widget_common.R.raw.query_recommendation_widget)

    @Provides
    @WishlistScope
    @Named("singleProductRecommendation")
    fun provideSingleProductRecommendationRawQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    com.tokopedia.recommendation_widget_common.R.raw.query_single_recommendation_widget)


    @Provides
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    com.tokopedia.atc_common.R.raw.mutation_add_to_cart)

    @Provides
    @Named(AtcConstant.MUTATION_UPDATE_CART_COUNTER)
    fun provideUpdateCartCounterMutation(@ApplicationContext context: Context): String =
        GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.gql_update_cart_counter)

    @Provides
    fun provideSendTopAdsUseCase() = SendTopAdsUseCase()
}