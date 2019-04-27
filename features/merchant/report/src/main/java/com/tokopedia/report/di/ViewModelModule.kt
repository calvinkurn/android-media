package com.tokopedia.report.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.report.view.viewmodel.ProductReportViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@MerchantReportScope
@Module
abstract class ViewModelModule {

    @MerchantReportScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductReportViewModel::class)
    internal abstract fun productInfoViewModel(viewModel: ProductReportViewModel): ViewModel
}