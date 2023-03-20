package com.tokopedia.checkout.view.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.checkout.view.ShipmentPresenter
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CheckoutPresenterModule {

    @Binds
    @CheckoutScope
    abstract fun bindViewModelFactory(viewModelFactor: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @CheckoutScope
    @IntoMap
    @ViewModelKey(ShipmentPresenter::class)
    abstract fun bindShipmentPresenter(shipmentPresenter: ShipmentPresenter): ViewModel
}
