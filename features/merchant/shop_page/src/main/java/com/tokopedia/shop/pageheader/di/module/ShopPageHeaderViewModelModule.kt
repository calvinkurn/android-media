package com.tokopedia.shop.pageheader.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.pageheader.di.scope.ShopPageHeaderScope
import com.tokopedia.shop.pageheader.presentation.ShopPageHeaderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopPageHeaderViewModelModule {

    @ShopPageHeaderScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopPageHeaderViewModel::class)
    internal abstract fun newShopPageViewModel(headerViewModel: ShopPageHeaderViewModel): ViewModel
}
