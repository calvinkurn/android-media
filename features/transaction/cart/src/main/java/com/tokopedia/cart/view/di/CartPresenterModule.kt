package com.tokopedia.cart.view.di

import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class CartPresenterModule {

    @Binds
    @CartScope
    abstract fun bindICartListPresenter(cartListPresenter: CartListPresenter): ICartListPresenter
}