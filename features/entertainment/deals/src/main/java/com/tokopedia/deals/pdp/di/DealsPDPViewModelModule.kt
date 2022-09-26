package com.tokopedia.deals.pdp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPAllLocationViewModel
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPSelectQuantityViewModel
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DealsPDPViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DealsPDPViewModel::class)
    abstract fun provideDealsPDPViewModel(viewModel: DealsPDPViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealsPDPAllLocationViewModel::class)
    abstract fun provideDealsPDPAllLocationViewModel(viewModel: DealsPDPAllLocationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealsPDPSelectQuantityViewModel::class)
    abstract fun provideDealsPDPSelectQuantityViewModel(viewModel: DealsPDPSelectQuantityViewModel): ViewModel

    @Binds
    @DealsPDPScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
