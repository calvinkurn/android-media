package com.tokopedia.product.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.detail.view.viewmodel.AddToCartDoneViewModel
import com.tokopedia.product.detail.view.viewmodel.ProductInfoViewModel
import com.tokopedia.product.detail.view.viewmodel.ProductInstallmentViewModel
import com.tokopedia.product.report.view.viewmodel.ProductReportViewModel
import com.tokopedia.product.warehouse.view.viewmodel.ProductWarehouseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ProductDetailScope
abstract class ViewModelModule {

    @ProductDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductInfoViewModel::class)
    internal abstract fun productInfoViewModel(viewModel: ProductInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductReportViewModel::class)
    internal abstract fun productReportViewModel(viewModel: ProductReportViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductWarehouseViewModel::class)
    internal abstract fun productWarehouseViewModel(viewModel: ProductWarehouseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductInstallmentViewModel::class)
    internal abstract fun productInstallmentViewModel(viewModel: ProductInstallmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddToCartDoneViewModel::class)
    internal abstract fun addToCartDoneViewModel(viewModel: AddToCartDoneViewModel): ViewModel

}