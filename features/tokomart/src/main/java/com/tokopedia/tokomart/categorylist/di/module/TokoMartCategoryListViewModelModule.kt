package com.tokopedia.tokomart.categorylist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokomart.categorylist.di.scope.TokoMartCategoryListScope
import com.tokopedia.tokomart.categorylist.presentation.viewmodel.TokoMartCategoryListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoMartCategoryListViewModelModule {

    @Binds
    @TokoMartCategoryListScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoMartCategoryListViewModel::class)
    internal abstract fun tokoMartCategoryListViewModel(viewModel: TokoMartCategoryListViewModel): ViewModel
}