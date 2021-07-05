package com.tokopedia.rechargegeneral.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.rechargegeneral.presentation.viewmodel.RechargeGeneralViewModel
import com.tokopedia.rechargegeneral.presentation.viewmodel.SharedRechargeGeneralViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RechargeGeneralViewModelModule {

    @RechargeGeneralScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RechargeGeneralViewModel::class)
    internal abstract fun digitalProductViewModel(viewModel: RechargeGeneralViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedRechargeGeneralViewModel::class)
    internal abstract fun sharedDigitalProductViewModel(viewModel: SharedRechargeGeneralViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DigitalAddToCartViewModel::class)
    abstract fun addToCartViewModel(viewModel: DigitalAddToCartViewModel): ViewModel
}