package com.tokopedia.shop.oldpage.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.oldpage.di.scope.ShopPageScope
import com.tokopedia.shop.oldpage.view.ShopPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ShopPageScope
abstract class ShopViewModelModule {

    @ShopPageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopPageViewModel::class)
    internal abstract fun shopPageViewModel(viewModel: ShopPageViewModel): ViewModel
}