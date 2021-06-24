package com.tokopedia.tokopedianow.categorylist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.categorylist.di.scope.CategoryListScope
import com.tokopedia.tokopedianow.categorylist.presentation.viewmodel.CategoryListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CategoryListViewModelModule {

    @Binds
    @CategoryListScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CategoryListViewModel::class)
    internal abstract fun tokoMartCategoryListViewModel(viewModel: CategoryListViewModel): ViewModel
}