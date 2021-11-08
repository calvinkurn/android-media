package com.tokopedia.tokopedianow.categoryfilter.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.categoryfilter.di.scope.CategoryFilterScope
import com.tokopedia.tokopedianow.categoryfilter.presentation.viewmodel.TokoNowCategoryFilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CategoryFilterViewModelModule {

    @Binds
    @CategoryFilterScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowCategoryFilterViewModel::class)
    internal abstract fun tokoNowCategoryFilterViewModel(viewModel: TokoNowCategoryFilterViewModel): ViewModel
}