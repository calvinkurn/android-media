package com.tokopedia.product.manage.feature.filter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterExpandChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterExpandSelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductManageFilterViewModelModule {

    @ProductManageFilterScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductManageFilterViewModel::class)
    internal abstract fun productManageFilterViewModel(viewModel: ProductManageFilterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductManageFilterExpandChecklistViewModel::class)
    internal abstract fun productManageFilterExpandChecklistViewModel(viewModel: ProductManageFilterExpandChecklistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductManageFilterExpandSelectViewModel::class)
    internal abstract fun productManageFilterExpandSelectViewModel(viewModel: ProductManageFilterExpandSelectViewModel): ViewModel
}