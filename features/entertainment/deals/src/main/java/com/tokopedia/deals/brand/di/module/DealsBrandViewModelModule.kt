package com.tokopedia.deals.brand.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.deals.brand.di.DealsBrandScope
import com.tokopedia.deals.brand.domain.viewmodel.DealsBrandViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DealsBrandViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DealsBrandViewModel::class)
    abstract fun provideDealsBrandViewModel(viewModel: DealsBrandViewModel): ViewModel

    @Binds
    @DealsBrandScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}