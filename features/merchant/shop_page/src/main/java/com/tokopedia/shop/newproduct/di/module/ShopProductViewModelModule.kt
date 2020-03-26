package com.tokopedia.shop.newproduct.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.newproduct.view.viewmodel.ShopPageProductListResultViewModel
import com.tokopedia.shop.newproduct.view.viewmodel.ShopPageProductListViewModel
import com.tokopedia.shop.newproduct.di.scope.ShopProductScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ShopProductScope
abstract class ShopProductViewModelModule {

    @ShopProductScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopPageProductListViewModel::class)
    internal abstract fun shopPageProductListViewModel(viewModel: ShopPageProductListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopPageProductListResultViewModel::class)
    internal abstract fun shopPageProductListResultViewModel(viewModel: ShopPageProductListResultViewModel): ViewModel
}