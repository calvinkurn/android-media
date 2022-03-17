package com.tokopedia.shopadmin.invitationaccepted.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shopadmin.invitationaccepted.di.component.AdminInvitationAcceptedComponent
import com.tokopedia.shopadmin.invitationaccepted.presentation.fragment.AdminInvitationAcceptedFragment

class AdminInvitationAcceptedActivity: BaseSimpleActivity(), HasComponent<AdminInvitationAcceptedComponent> {

    override fun getNewFragment(): Fragment {
        return AdminInvitationAcceptedFragment.newInstance()
    }

    override fun getComponent(): AdminInvitationAcceptedComponent {
        return DaggerAdminInvitationAcceptedComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

}