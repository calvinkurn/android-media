package com.tokopedia.digital_checkout.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.digital_checkout.presentation.viewmodel.DigitalCartViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 08/01/21
 */

@Module
@DigitalCheckoutScope
abstract class DigitalCheckoutViewModelModule {

    @DigitalCheckoutScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DigitalCartViewModel::class)
    abstract fun bindDigitalCartViewModel(viewModel: DigitalCartViewModel): ViewModel
}