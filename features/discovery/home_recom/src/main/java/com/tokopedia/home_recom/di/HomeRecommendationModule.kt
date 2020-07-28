package com.tokopedia.home_recom.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.domain.usecases.GetPrimaryProductUseCase
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcherImpl
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * A class module for dagger recommendation page
 */
@Module(includes = [TopAdsWishlistModule::class])
class HomeRecommendationModule {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @HomeRecommendationScope
    @Provides
    fun provideDispatchers(): RecommendationDispatcher = RecommendationDispatcherImpl()

    @Provides
    @HomeRecommendationScope
    fun provideAddWishlistUseCase(@ApplicationContext context: Context): AddWishListUseCase = AddWishListUseCase(context)

    @Provides
    @HomeRecommendationScope
    fun provideRemoveWishlistUseCase(@ApplicationContext context: Context): RemoveWishListUseCase = RemoveWishListUseCase(context)

    @Provides
    @HomeRecommendationScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    @HomeRecommendationScope
    fun provideGetRecommendationUseCase(@ApplicationContext context: Context,
                                        graphqlUseCase: GraphqlUseCase,
                                        userSessionInterface: UserSessionInterface): GetRecommendationUseCase {
        val recomQuery = GraphqlHelper.loadRawString(context.resources, com.tokopedia.recommendation_widget_common.R.raw.query_recommendation_widget)
        return GetRecommendationUseCase(recomQuery, graphqlUseCase, userSessionInterface)
    }
    @Provides
    @HomeRecommendationScope
    fun provideGetSingleRecommendationUseCase(@ApplicationContext context: Context, graphqlUseCase: GraphqlUseCase, userSessionInterface: UserSessionInterface): GetSingleRecommendationUseCase {
        val recomQuery = GraphqlHelper.loadRawString(context.resources, com.tokopedia.recommendation_widget_common.R.raw.gql_single_product_recommendation)
        return GetSingleRecommendationUseCase(recomQuery, graphqlUseCase, userSessionInterface)
    }

    @Provides
    @HomeRecommendationScope
    fun provideGetPrimaryProductUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, userSessionInterface: UserSessionInterface): GetPrimaryProductUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_primary_product)
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<PrimaryProductEntity>(graphqlRepository)
        useCase.setGraphqlQuery(query)
        return GetPrimaryProductUseCase(useCase)
    }

    @Provides
    @HomeRecommendationScope
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    com.tokopedia.atc_common.R.raw.mutation_add_to_cart)

}