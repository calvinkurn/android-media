package com.tokopedia.product.manage.common.feature.quickedit.stock.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.viewmodel.ProductManageQuickEditStockViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductManageQuickEditViewModelModule {

    @ProductManageQuickEditStockScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductManageQuickEditStockViewModel::class)
    internal abstract fun productManageFilterViewModel(viewModel: ProductManageQuickEditStockViewModel): ViewModel
}