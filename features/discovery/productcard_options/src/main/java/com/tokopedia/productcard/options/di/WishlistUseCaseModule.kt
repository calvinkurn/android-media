package com.tokopedia.productcard.options.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides

@Module
internal class WishlistUseCaseModule {

    @ProductCardOptionsScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ProductCardOptionsScope
    @Provides
    fun provideAddWishlistUseCaseModule(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @ProductCardOptionsScope
    @Provides
    fun provideRemoveWishlistUseCaseModule(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }
}