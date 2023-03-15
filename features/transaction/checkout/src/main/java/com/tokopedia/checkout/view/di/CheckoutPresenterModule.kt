package com.tokopedia.checkout.view.di

import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class CheckoutPresenterModule {

    @Binds
    @CheckoutScope
    abstract fun bindShipmentPresenter(shipmentPresenter: ShipmentPresenter): ShipmentContract.Presenter
}
