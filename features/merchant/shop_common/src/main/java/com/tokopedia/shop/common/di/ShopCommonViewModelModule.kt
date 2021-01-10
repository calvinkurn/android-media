package com.tokopedia.shop.common.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.common.view.viewmodel.AdminRoleAuthorizeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@ShopCommonScope
@Module
abstract class ShopCommonViewModelModule {

    @ShopCommonScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AdminRoleAuthorizeViewModel::class)
    internal abstract fun provideAdminRoleAuthorizeViewModel(adminRoleAuthorizeViewModel: AdminRoleAuthorizeViewModel)

}