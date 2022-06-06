package com.tokopedia.productcard.options.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.productcard.options.FLAG_IS_USING_ADD_REMOVE_WISHLIST_V2
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.util.WishlistV2RemoteConfigRollenceUtil
import dagger.Module
import dagger.Provides
import javax.inject.Named

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
    fun provideAddToWishlistV2UseCaseModule(@ApplicationContext graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @ProductCardOptionsScope
    @Provides
    fun provideDeleteWishlistV2UseCaseModule(@ApplicationContext graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }

    @ProductCardOptionsScope
    @Provides
    @Named(FLAG_IS_USING_ADD_REMOVE_WISHLIST_V2)
    fun provideFlagIsUsingWishlistV2(@ApplicationContext context: Context): Boolean {
        return WishlistV2RemoteConfigRollenceUtil.isUsingAddRemoveWishlistV2(context)
    }
}