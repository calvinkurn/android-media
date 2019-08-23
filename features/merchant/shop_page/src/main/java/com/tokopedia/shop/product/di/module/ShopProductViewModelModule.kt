package com.tokopedia.shop.product.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.product.di.scope.ShopProductScope
import com.tokopedia.shop.product.view.viewmodel.ShopProductLimitedViewModel
import com.tokopedia.shop.product.view.viewmodel.ShopProductListViewModel
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
    @ViewModelKey(ShopProductLimitedViewModel::class)
    internal abstract fun shopProductLimitedViewModel(viewModel: ShopProductLimitedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopProductListViewModel::class)
    internal abstract fun shopProductViewModel(viewModel: ShopProductListViewModel): ViewModel
}