package com.tokopedia.manageaddress.di.shoplocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.manageaddress.ui.shoplocation.ShopLocationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopLocationViewModelModule {


    @ShopLocationScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ShopLocationScope
    @Binds
    @IntoMap
    @ViewModelKey(ShopLocationViewModel::class)
    internal abstract fun providesShopLocationViewModel(viewModel: ShopLocationViewModel): ViewModel

}