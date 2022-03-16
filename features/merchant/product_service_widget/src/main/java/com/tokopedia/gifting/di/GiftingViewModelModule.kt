package com.tokopedia.gifting.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.gifting.presentation.viewmodel.GiftingViewModel
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GiftingViewModelModule {

    @GiftingScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GiftingViewModel::class)
    abstract fun provideGiftingViewModel(viewModel: GiftingViewModel): ViewModel
}