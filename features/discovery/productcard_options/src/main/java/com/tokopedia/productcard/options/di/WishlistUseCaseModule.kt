package com.tokopedia.productcard.options.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides

@Module
internal class WishlistUseCaseModule {

    @ProductCardOptionsScope
    @Provides
    fun provideAddWishlistUseCaseModule(@ApplicationContext context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @ProductCardOptionsScope
    @Provides
    fun provideRemoveWishlistUseCaseModule(@ApplicationContext context: Context): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @ProductCardOptionsScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ProductCardOptionsScope
    @Provides
    fun provideAddToWishlistV2UseCaseModule(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @ProductCardOptionsScope
    @Provides
    fun provideDeleteWishlistV2UseCaseModule(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }
}