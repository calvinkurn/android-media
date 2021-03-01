package com.tokopedia.shop.pageheader.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.pageheader.di.scope.ShopPageScope
import com.tokopedia.shop.pageheader.presentation.ShopPageViewModel
import com.tokopedia.shop.pageheader.presentation.NewShopPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopViewModelModule {

    @ShopPageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopPageViewModel::class)
    internal abstract fun shopPageViewModel(viewModel: ShopPageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewShopPageViewModel::class)
    internal abstract fun newShopPageViewModel(viewModel: NewShopPageViewModel): ViewModel
}