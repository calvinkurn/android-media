package com.tokopedia.similarsearch.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides

@Module
internal class WishlistUseCaseModule {

    @SimilarSearchModuleScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @SimilarSearchModuleScope
    @Provides
    fun provideAddToWishlistV2UseCase(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @SimilarSearchModuleScope
    @Provides
    fun provideDeleteWishlistV2UseCase(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }
}