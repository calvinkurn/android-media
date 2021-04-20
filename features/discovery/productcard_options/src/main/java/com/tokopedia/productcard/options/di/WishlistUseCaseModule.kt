package com.tokopedia.productcard.options.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
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
}