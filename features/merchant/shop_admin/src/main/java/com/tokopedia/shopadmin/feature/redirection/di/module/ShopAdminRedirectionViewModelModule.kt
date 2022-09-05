package com.tokopedia.shopadmin.feature.redirection.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shopadmin.feature.redirection.di.scope.ShopAdminRedirectionScope
import com.tokopedia.shopadmin.feature.redirection.presentation.viewmodel.ShopAdminRedirectionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopAdminRedirectionViewModelModule {

    @ShopAdminRedirectionScope
    @Binds
    abstract fun bindShopAdminRedirectionViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopAdminRedirectionViewModel::class)
    abstract fun shopAdminRedirectionViewModel(shopAdminRedirectionViewModel: ShopAdminRedirectionViewModel): ViewModel
}