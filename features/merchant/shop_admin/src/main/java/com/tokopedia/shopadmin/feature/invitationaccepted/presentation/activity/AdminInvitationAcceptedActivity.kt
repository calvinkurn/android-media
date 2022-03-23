package com.tokopedia.shopadmin.feature.invitationaccepted.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.UriUtil
import com.tokopedia.shopadmin.common.constants.Constants
import com.tokopedia.shopadmin.feature.invitationaccepted.di.component.AdminInvitationAcceptedComponent
import com.tokopedia.shopadmin.feature.invitationaccepted.di.component.DaggerAdminInvitationAcceptedComponent
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.fragment.AdminInvitationAcceptedFragment

class AdminInvitationAcceptedActivity: BaseSimpleActivity(), HasComponent<AdminInvitationAcceptedComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.data?.let {
            Bundle().apply {
                val params = UriUtil.uriQueryParamsToMap(it)
                val shopNameParam = params[Constants.SHOP_NAME_PARAM].orEmpty()
                putString(Constants.SHOP_NAME_PARAM, shopNameParam)
            }
        }
        return AdminInvitationAcceptedFragment.newInstance(bundle)
    }

    override fun getComponent(): AdminInvitationAcceptedComponent {
        return DaggerAdminInvitationAcceptedComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    private fun hideToolbar() {
        supportActionBar?.hide()
    }

}