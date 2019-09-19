package com.tokopedia.shop.info.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.info.di.scope.ShopInfoScope
import com.tokopedia.shop.info.view.viewmodel.ShopInfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ShopInfoScope
abstract class ShopInfoViewModelModule {

    @ShopInfoScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopInfoViewModel::class)
    internal abstract fun shopInfoViewModel(viewModel: ShopInfoViewModel): ViewModel
}