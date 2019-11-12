package com.tokopedia.similarsearch

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.similarsearch.di.scope.SimilarSearchModuleScope
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides

@SimilarSearchModuleScope
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
}