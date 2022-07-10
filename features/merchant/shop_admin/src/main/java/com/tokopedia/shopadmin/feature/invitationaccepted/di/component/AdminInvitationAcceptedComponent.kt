package com.tokopedia.shopadmin.feature.invitationaccepted.di.component

import com.tokopedia.shopadmin.common.di.ShopAdminComponent
import com.tokopedia.shopadmin.feature.invitationaccepted.di.module.AdminInvitationAcceptedModule
import com.tokopedia.shopadmin.feature.invitationaccepted.di.scope.AdminInvitationAcceptedScope
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.bottomsheet.TncAdminBottomSheet
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.fragment.AdminInvitationAcceptedFragment
import dagger.Component

@AdminInvitationAcceptedScope
@Component(modules = [AdminInvitationAcceptedModule::class], dependencies = [ShopAdminComponent::class])
interface AdminInvitationAcceptedComponent {
    fun inject(fragment: AdminInvitationAcceptedFragment)
    fun inject(bottomSheet: TncAdminBottomSheet)
}