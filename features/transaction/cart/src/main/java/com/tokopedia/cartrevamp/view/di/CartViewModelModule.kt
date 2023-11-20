package com.tokopedia.cartrevamp.view.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.cartrevamp.view.CartViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CartViewModelModule {

    @Binds
    @ActivityScope
    abstract fun bindViewModelFactory(viewModelFactor: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(CartViewModel::class)
    abstract fun bindCartViewModel(cartViewModel: CartViewModel): ViewModel
}
