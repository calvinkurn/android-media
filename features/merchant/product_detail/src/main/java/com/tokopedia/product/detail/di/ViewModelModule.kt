package com.tokopedia.product.detail.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.detail.view.viewmodel.ProductInfoViewModel
import com.tokopedia.product.report.view.viewmodel.ProductReportViewModel
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

}