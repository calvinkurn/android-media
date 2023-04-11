package com.tokopedia.product_ar.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product_ar.viewmodel.ProductArComparissonViewModel
import com.tokopedia.product_ar.viewmodel.ProductArViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductArViewModelModule {

    @ProductArScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductArViewModel::class)
    internal abstract fun provideProductArViewModel(viewModel: ProductArViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductArComparissonViewModel::class)
    internal abstract fun provideProductArComparisonViewModel(viewModel: ProductArComparissonViewModel): ViewModel

}