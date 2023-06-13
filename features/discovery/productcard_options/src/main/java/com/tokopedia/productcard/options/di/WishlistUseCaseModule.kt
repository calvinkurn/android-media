package com.tokopedia.productcard.options.di

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides

@Module
internal class WishlistUseCaseModule {

    @ProductCardOptionsScope
    @Provides
    fun provideAddToWishlistV2UseCaseModule(@ApplicationContext graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @ProductCardOptionsScope
    @Provides
    fun provideDeleteWishlistV2UseCaseModule(@ApplicationContext graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }
}