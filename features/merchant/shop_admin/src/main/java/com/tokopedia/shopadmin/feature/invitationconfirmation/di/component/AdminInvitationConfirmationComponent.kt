package com.tokopedia.shopadmin.feature.invitationconfirmation.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shopadmin.feature.invitationconfirmation.di.module.AdminInvitationConfirmationModule
import com.tokopedia.shopadmin.feature.invitationconfirmation.di.scope.AdminInvitationConfirmationScope
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.fragment.AdminInvitationConfirmationFragment
import dagger.Component

@AdminInvitationConfirmationScope
@Component(modules = [AdminInvitationConfirmationModule::class], dependencies = [BaseAppComponent::class])
interface AdminInvitationConfirmationComponent {
    fun inject(fragment: AdminInvitationConfirmationFragment)
}