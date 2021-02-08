package com.tokopedia.loginregister.shopcreation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.loginregister.shopcreation.viewmodel.ShopCreationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Ade Fulki on 2019-12-09.
 * ade.hadian@tokopedia.com
 */

@Module
abstract class ShopCreationViewModelModule {

    @Binds
    @ShopCreationScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopCreationViewModel::class)
    internal abstract fun shopCreationViewModel(viewModel: ShopCreationViewModel): ViewModel
}