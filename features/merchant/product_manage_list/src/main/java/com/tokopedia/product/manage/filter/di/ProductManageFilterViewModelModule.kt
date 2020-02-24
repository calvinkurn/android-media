package com.tokopedia.product.manage.filter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.manage.filter.presentation.viewmodel.ProductManageFilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ProductManageFilterScope
abstract class ProductManageFilterViewModelModule {

    @ProductManageFilterScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductManageFilterViewModel::class)
    internal abstract fun productManageFilterViewModel(viewModel: ProductManageFilterViewModel): ViewModel
}