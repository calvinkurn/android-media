package com.tokopedia.manageaddress.di.shoplocation

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@ShopLocationScope
@Module
abstract class ShopLocationViewModelModule {


    @ShopLocationScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}