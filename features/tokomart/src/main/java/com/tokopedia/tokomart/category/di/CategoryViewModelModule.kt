package com.tokopedia.tokomart.category.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokomart.category.domain.usecase.GetCategoryUseCaseModule
import com.tokopedia.tokomart.category.presentation.viewmodel.CategoryViewModel
import com.tokopedia.tokomart.searchcategory.domain.usecase.GetFilterUseCaseModule
import com.tokopedia.tokomart.searchcategory.domain.usecase.GetProductCountUseCaseModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [
    GetCategoryUseCaseModule::class,
    GetFilterUseCaseModule::class,
    GetProductCountUseCaseModule::class
])
abstract class CategoryViewModelModule {

    @CategoryScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    internal abstract fun categoryViewModel(viewModel: CategoryViewModel): ViewModel
}