package com.tokopedia.new_product_bundle.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.new_product_bundle.viewmodel.ProductBundleViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductBundleViewModelModule {

    @ProductBundleScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductBundleViewModel::class)
    abstract fun provideProductViewModel(productBundleViewModel: ProductBundleViewModel): ViewModel
}