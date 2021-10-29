package com.tokopedia.deals.brand_detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.deals.brand_detail.ui.viewmodel.DealsBrandDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DealsBrandDetailViewModelModule {

    @Binds
    @DealsBrandDetailScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DealsBrandDetailViewModel::class)
    abstract fun dealsBrandDetailViewModel(viewModel: DealsBrandDetailViewModel): ViewModel
}