package com.tokopedia.cart.bundle.view.di

import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.ICartListPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class CartPresenterModule {

    @Binds
    @CartScope
    abstract fun bindICartListPresenter(cartListPresenter: CartListPresenter): ICartListPresenter
}