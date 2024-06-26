package com.tokopedia.pdpsimulation.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.PayLaterActivationViewModel
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PayLaterViewModel::class)
    internal abstract fun bindsPayLaterViewModel(viewModel: PayLaterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PayLaterActivationViewModel::class)
    internal abstract fun bindsPayLaterActivationViewModel(viewModel: PayLaterActivationViewModel): ViewModel


}