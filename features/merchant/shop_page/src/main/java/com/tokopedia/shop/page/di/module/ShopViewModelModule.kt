package com.tokopedia.shop.page.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.page.di.scope.ShopPageScope
import com.tokopedia.shop.page.view.ShopPageViewModel
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