package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shopadmin.feature.invitationconfirmation.di.component.AdminInvitationConfirmationComponent
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.fragment.AdminInvitationConfirmationFragment

class AdminInvitationConfirmationActivity: BaseSimpleActivity(), HasComponent<AdminInvitationConfirmationComponent> {

    override fun getNewFragment(): Fragment {
        return AdminInvitationConfirmationFragment.newInstance()
    }

    override fun getComponent(): AdminInvitationConfirmationComponent {
        return DaggerAdminInvitationConfirmationComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

}