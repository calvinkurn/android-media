package com.tokopedia.createpost.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.createpost.view.plist.ShopPageProductListViewModel
import com.tokopedia.createpost.view.viewmodel.CreateContentPostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CreateContentPostViewModel::class)
    internal abstract fun bindsPaymentListViewModel(viewModel: CreateContentPostViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ShopPageProductListViewModel::class)
    internal abstract fun shopPageProductViewModel(viewModel: ShopPageProductListViewModel): ViewModel



}