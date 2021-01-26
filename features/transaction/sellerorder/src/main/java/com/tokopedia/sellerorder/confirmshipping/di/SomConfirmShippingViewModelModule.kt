package com.tokopedia.sellerorder.confirmshipping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.confirmshipping.presentation.viewmodel.SomConfirmShippingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fwidjaja on 2019-11-15.
 */
@Module
abstract class SomConfirmShippingViewModelModule {
    @SomConfirmShippingScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SomConfirmShippingViewModel::class)
    internal abstract fun somConfirmShippingViewModel(viewModel: SomConfirmShippingViewModel): ViewModel
}