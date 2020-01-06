package com.tokopedia.shop.pageheader.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.pageheader.presentation.viewmodel.ShopPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ShopPageScope
abstract class ShopPageViewModelModule {

    @Binds
    @ShopPageScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopPageViewModel::class)
    internal abstract fun officialStoreHomeViewModel(viewModel: ShopPageViewModel): ViewModel
}
