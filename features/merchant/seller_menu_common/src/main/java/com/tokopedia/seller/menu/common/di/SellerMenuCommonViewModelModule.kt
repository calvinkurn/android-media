package com.tokopedia.seller.menu.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.seller.menu.common.view.viewmodel.AdminRoleAuthorizeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SellerMenuCommonViewModelModule {

    @SellerMenuCommonScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AdminRoleAuthorizeViewModel::class)
    internal abstract fun provideAdminRoleAuthorizeViewModel(adminRoleAuthorizeViewModel: AdminRoleAuthorizeViewModel): ViewModel

}