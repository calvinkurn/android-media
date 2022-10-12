package com.tokopedia.search.result.product.addtocart

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class AddToCartModule {

    @SearchScope
    @Binds
    abstract fun provideAddToCartView(provider: AddToCartViewDelegate): AddToCartView

    @SearchScope
    @Binds
    abstract fun provideAddToCartPresenter(provider: AddToCartPresenterDelegate): AddToCartPresenter
}
