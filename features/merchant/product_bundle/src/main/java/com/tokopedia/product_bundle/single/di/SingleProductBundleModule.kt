package com.tokopedia.product_bundle.single.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product_bundle.single.presentation.viewmodel.SingleProductBundleViewModel
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SingleProductBundleModule {

    @SingleProductBundleScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SingleProductBundleViewModel::class)
    abstract fun provideSingleProductViewModel(viewModel: SingleProductBundleViewModel): ViewModel
}