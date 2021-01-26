package com.tokopedia.product.manage.feature.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductDraftListCountViewModel
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract  class ViewModelModule {

    @Binds
    @ProductManageListScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductManageViewModel::class)
    internal abstract fun productManageViewModel(viewModel: ProductManageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductDraftListCountViewModel::class)
    internal abstract fun productDraftListCountViewModel(viewModel: ProductDraftListCountViewModel): ViewModel
}