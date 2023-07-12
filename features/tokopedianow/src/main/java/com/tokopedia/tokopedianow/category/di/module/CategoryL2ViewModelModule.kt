package com.tokopedia.tokopedianow.category.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.category.di.scope.CategoryL2Scope
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2ViewModel
import com.tokopedia.tokopedianow.searchcategory.di.GraphqlModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [GraphqlModule::class])
abstract class CategoryL2ViewModelModule {

    @CategoryL2Scope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @CategoryL2Scope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowCategoryL2ViewModel::class)
    internal abstract fun categoryL2ViewModel(viewModel: TokoNowCategoryL2ViewModel): ViewModel
}
