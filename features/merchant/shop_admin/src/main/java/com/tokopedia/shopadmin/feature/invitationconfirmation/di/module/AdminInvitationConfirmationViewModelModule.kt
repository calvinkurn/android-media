package com.tokopedia.shopadmin.feature.invitationconfirmation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shopadmin.feature.invitationconfirmation.di.scope.AdminInvitationConfirmationScope
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.viewmodel.AdminInvitationConfirmationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AdminInvitationConfirmationViewModelModule {

    @AdminInvitationConfirmationScope
    @Binds
    abstract fun bindViewModelShopPerformanceFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AdminInvitationConfirmationViewModel::class)
    abstract fun adminConfirmationInvitationViewModel(adminInvitationConfirmationViewModel: AdminInvitationConfirmationViewModel): ViewModel
}