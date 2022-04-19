package com.tokopedia.product.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides

@Module
class ProductWishlistModule {

    @ProductDetailScope
    @Provides
    fun provideRemoveWishListUseCase(@ApplicationContext context: Context): RemoveWishListUseCase =
            RemoveWishListUseCase(context)

    @ProductDetailScope
    @Provides
    fun provideAddWishListUseCase(@ApplicationContext context: Context): AddWishListUseCase =
            AddWishListUseCase(context)
}