package com.tokopedia.tokopedianow.category.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.category.di.scope.CategoryScope
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryBaseViewModel
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryMainViewModel
import com.tokopedia.tokopedianow.searchcategory.di.GraphqlModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [
    CategoryUseCaseModule::class,
    GraphqlModule::class,
])
abstract class CategoryViewModelModule {

    @CategoryScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @CategoryScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowCategoryMainViewModel::class)
    internal abstract fun categoryMainViewModel(viewModel: TokoNowCategoryMainViewModel): ViewModel

    @CategoryScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowCategoryBaseViewModel::class)
    internal abstract fun categoryBaseViewModel(viewModel: TokoNowCategoryBaseViewModel): ViewModel
}
