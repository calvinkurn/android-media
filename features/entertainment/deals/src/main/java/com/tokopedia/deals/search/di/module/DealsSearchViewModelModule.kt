package com.tokopedia.deals.search.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.search.di.DealsSearchScope
import com.tokopedia.deals.search.domain.viewmodel.DealsSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DealsSearchViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DealsSearchViewModel::class)
    abstract fun provideDealsSearchViewModel(viewModel: DealsSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealsBaseViewModel::class)
    abstract fun dealsBaseViewModel(viewModel: DealsBaseViewModel): ViewModel

    @Binds
    @DealsSearchScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}