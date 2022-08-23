package com.tokopedia.similarsearch.di

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

    @SimilarSearchModuleScope
    @Provides
    fun provideAddWishlistUseCase(@ApplicationContext context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @SimilarSearchModuleScope
    @Provides
    fun provideRemoveWishlistUseCase(@ApplicationContext context: Context): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

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