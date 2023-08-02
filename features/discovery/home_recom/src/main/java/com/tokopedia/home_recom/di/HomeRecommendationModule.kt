package com.tokopedia.home_recom.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_recom.domain.query.PrimaryProductQuery
import com.tokopedia.home_recom.domain.usecases.GetPrimaryProductUseCase
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcherImpl
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.GetTopadsIsAdsUseCase
import com.tokopedia.topads.sdk.domain.model.TopadsIsAdsQuery
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides

/**
 * A class module for dagger recommendation page
 */
@Module(includes = [TopAdsWishlistModule::class])
class HomeRecommendationModule {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @HomeRecommendationScope
    @Provides
    fun provideGraphqlUseCase(graphqlRepository: GraphqlRepository) =
        com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>(graphqlRepository)

    @HomeRecommendationScope
    @Provides
    fun provideDispatchers(): RecommendationDispatcher = RecommendationDispatcherImpl()

    @Provides
    @HomeRecommendationScope
    fun provideAddToWishlistV2UseCase(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase = AddToWishlistV2UseCase(graphqlRepository)

    @Provides
    @HomeRecommendationScope
    fun provideDeleteWishlistV2UseCase(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase = DeleteWishlistV2UseCase(graphqlRepository)

    @Provides
    @HomeRecommendationScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    @HomeRecommendationScope
    fun provideGetTopadsIsAdsUseCase(
        graphqlUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>,
        irisSession: TopAdsIrisSession
    ): GetTopadsIsAdsUseCase {
        return GetTopadsIsAdsUseCase(graphqlUseCase as com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<TopadsIsAdsQuery>, irisSession)
    }

    @Provides
    @HomeRecommendationScope
    fun provideGetPrimaryProductUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): GetPrimaryProductUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<PrimaryProductEntity>(graphqlRepository)
        useCase.setGraphqlQuery(PrimaryProductQuery())
        return GetPrimaryProductUseCase(context, useCase)
    }

    @Provides
    @HomeRecommendationScope
    fun provideGetTopAdsHeadlineUseCase(graphqlRepository: GraphqlRepository): GetTopAdsHeadlineUseCase {
        return GetTopAdsHeadlineUseCase(graphqlRepository)
    }

    @Provides
    @HomeRecommendationScope
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}
