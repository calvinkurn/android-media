package com.tokopedia.shop.newinfo.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.newinfo.di.scope.ShopNewInfoScope
import com.tokopedia.shop.newinfo.view.viewmodel.ShopNewInfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@ShopNewInfoScope
@Module
abstract class ShopNewInfoViewModelModule {

    @ShopNewInfoScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopNewInfoViewModel::class)
    abstract fun provideShopNewInfoViewModel(viewModel: ShopNewInfoViewModel): ViewModel
}