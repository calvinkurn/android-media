package com.tokopedia.shop.search.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.search.di.scope.ShopSearchProductScope
import com.tokopedia.shop.search.view.viewmodel.ShopSearchProductViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopSearchProductViewModelModule {

    @ShopSearchProductScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopSearchProductViewModel::class)
    internal abstract fun shopSearchProductViewModel(viewModel: ShopSearchProductViewModel): ViewModel
}