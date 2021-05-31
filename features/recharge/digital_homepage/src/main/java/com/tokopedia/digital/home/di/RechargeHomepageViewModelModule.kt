package com.tokopedia.digital.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageSearchViewModel
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RechargeHomepageViewModelModule {

    @RechargeHomepageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DigitalHomePageSearchViewModel::class)
    internal abstract fun digitalHomepageSearchViewModel(viewModel: DigitalHomePageSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RechargeHomepageViewModel::class)
    internal abstract fun rechargeHomepageViewModel(viewModel: RechargeHomepageViewModel): ViewModel

}