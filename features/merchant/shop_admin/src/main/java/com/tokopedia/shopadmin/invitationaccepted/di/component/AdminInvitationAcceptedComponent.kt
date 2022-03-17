package com.tokopedia.shopadmin.invitationaccepted.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shopadmin.invitationaccepted.di.module.AdminInvitationAcceptedModule
import com.tokopedia.shopadmin.invitationaccepted.di.scope.AdminInvitationAcceptedScope
import com.tokopedia.shopadmin.invitationaccepted.presentation.fragment.AdminInvitationAcceptedFragment
import dagger.Component

@AdminInvitationAcceptedScope
@Component(modules = [AdminInvitationAcceptedModule::class], dependencies = [BaseAppComponent::class])
interface AdminInvitationAcceptedComponent {
    fun inject(fragment: AdminInvitationAcceptedFragment)
}