package com.tokopedia.shopadmin.feature.authorize.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shopadmin.feature.authorize.di.scope.AdminRoleAuthorizeScope
import com.tokopedia.shopadmin.feature.authorize.presentation.viewmodel.AdminRoleAuthorizeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AdminRoleAuthorizeViewModelModule {

    @AdminRoleAuthorizeScope
    @Binds
    abstract fun bindAdminRoleAuthorizeViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AdminRoleAuthorizeViewModel::class)
    internal abstract fun provideAdminRoleAuthorizeViewModel(adminRoleAuthorizeViewModel: AdminRoleAuthorizeViewModel): ViewModel

}
