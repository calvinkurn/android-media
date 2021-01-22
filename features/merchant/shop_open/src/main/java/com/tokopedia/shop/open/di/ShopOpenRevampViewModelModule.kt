package com.tokopedia.shop.open.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.open.presentation.viewmodel.ShopOpenRevampViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopOpenRevampViewModelModule {

    @ShopOpenRevampScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopOpenRevampViewModel::class)
    internal abstract fun shopOpenRevampViewModel(viewModel: ShopOpenRevampViewModel): ViewModel

}