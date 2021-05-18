package com.tokopedia.seller.menu.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.seller.menu.di.scope.SellerMenuScope
import com.tokopedia.seller.menu.presentation.viewmodel.AdminRoleAuthorizeViewModel
import com.tokopedia.seller.menu.presentation.viewmodel.SellerMenuViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @SellerMenuScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SellerMenuViewModel::class)
    internal abstract fun sellerMenuViewModel(viewModel: SellerMenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AdminRoleAuthorizeViewModel::class)
    internal abstract fun provideAdminRoleAuthorizeViewModel(adminRoleAuthorizeViewModel: AdminRoleAuthorizeViewModel): ViewModel
}