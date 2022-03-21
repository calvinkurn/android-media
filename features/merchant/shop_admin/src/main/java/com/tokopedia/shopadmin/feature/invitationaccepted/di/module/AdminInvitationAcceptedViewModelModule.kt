package com.tokopedia.shopadmin.feature.invitationaccepted.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shopadmin.feature.invitationaccepted.di.scope.AdminInvitationAcceptedScope
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.viewmodel.AdminInvitationAcceptedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AdminInvitationAcceptedViewModelModule {

    @AdminInvitationAcceptedScope
    @Binds
    abstract fun bindViewModelShopPerformanceFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AdminInvitationAcceptedViewModel::class)
    abstract fun adminInvitationAcceptedViewModel(adminInvitationAcceptedViewModel: AdminInvitationAcceptedViewModel): ViewModel
}