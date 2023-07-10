package com.tokopedia.checkout.view.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.checkout.view.ShipmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CheckoutViewModelModule {

    @Binds
    @ActivityScope
    abstract fun bindViewModelFactory(viewModelFactor: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ShipmentViewModel::class)
    abstract fun bindShipmentViewModel(shipmentViewModel: ShipmentViewModel): ViewModel
}
