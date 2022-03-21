package com.tokopedia.shopadmin.feature.invitationaccepted.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shopadmin.feature.invitationaccepted.di.module.AdminInvitationAcceptedModule
import com.tokopedia.shopadmin.feature.invitationaccepted.di.scope.AdminInvitationAcceptedScope
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.fragment.AdminInvitationAcceptedFragment
import dagger.Component

@AdminInvitationAcceptedScope
@Component(modules = [AdminInvitationAcceptedModule::class], dependencies = [BaseAppComponent::class])
interface AdminInvitationAcceptedComponent {
    fun inject(fragment: AdminInvitationAcceptedFragment)
}