package com.tokopedia.search.result.product.wishlist

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class WishlistModule {

    @Binds
    @SearchScope
    abstract fun provideWishlistView(wishlistViewDelegate: WishlistViewDelegate): WishlistView
}
