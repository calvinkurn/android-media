package com.tokopedia.oldproductbundle.single.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.oldproductbundle.single.presentation.viewmodel.SingleProductBundleViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SingleProductBundleViewModelModule {

    @SingleProductBundleScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SingleProductBundleViewModel::class)
    abstract fun provideSingleProductViewModel(viewModel: SingleProductBundleViewModel): ViewModel
}