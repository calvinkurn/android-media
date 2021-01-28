package com.tokopedia.report.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.report.view.viewmodel.ProductReportSubmitViewModel
import com.tokopedia.report.view.viewmodel.ProductReportViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @MerchantReportScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductReportViewModel::class)
    internal abstract fun productInfoViewModel(viewModel: ProductReportViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductReportSubmitViewModel::class)
    internal abstract fun productReportSubmitViewModel(viewModel: ProductReportSubmitViewModel): ViewModel
}