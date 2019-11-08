package com.tokopedia.product.detail.imagepreview.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.product.detail.imagepreview.di.ImagePreviewPDPScope
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides

@ImagePreviewPDPScope
@Module
class ImagePreviewPDPWishlistModule {

    @ImagePreviewPDPScope
    @Provides
    fun provideRemoveWishListUseCase(@ApplicationContext context: Context): RemoveWishListUseCase = RemoveWishListUseCase(context)

    @ImagePreviewPDPScope
    @Provides
    fun provideAddWishListUseCase(@ApplicationContext context: Context): AddWishListUseCase = AddWishListUseCase(context)
}