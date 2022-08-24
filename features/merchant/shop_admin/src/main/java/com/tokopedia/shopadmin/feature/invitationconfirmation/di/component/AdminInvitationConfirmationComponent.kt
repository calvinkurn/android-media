package com.tokopedia.shopadmin.feature.invitationconfirmation.di.component

import com.tokopedia.shopadmin.common.di.ShopAdminComponent
import com.tokopedia.shopadmin.feature.invitationconfirmation.di.module.AdminInvitationConfirmationModule
import com.tokopedia.shopadmin.feature.invitationconfirmation.di.scope.AdminInvitationConfirmationScope
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.fragment.AdminInvitationConfirmationFragment
import dagger.Component

@AdminInvitationConfirmationScope
@Component(modules = [AdminInvitationConfirmationModule::class], dependencies = [ShopAdminComponent::class])
interface AdminInvitationConfirmationComponent {
    fun inject(fragment: AdminInvitationConfirmationFragment)
}