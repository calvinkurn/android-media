package com.tokopedia.sellerorder.requestpickup.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.requestpickup.presentation.viewmodel.SomConfirmReqPickupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fwidjaja on 2019-09-30.
 */

@Module
abstract class SomConfirmReqPickupViewModelModule {
    @SomConfirmReqPickupScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SomConfirmReqPickupViewModel::class)
    internal abstract fun somConfirmRequestPickupViewModel(viewModel: SomConfirmReqPickupViewModel): ViewModel
}