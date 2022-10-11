package com.tokopedia.managepassword.addpassword.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.managepassword.addpassword.view.fragment.AddPasswordFragment
import com.tokopedia.managepassword.di.DaggerManagePasswordComponent
import com.tokopedia.managepassword.di.ManagePasswordComponent
import com.tokopedia.managepassword.di.module.ManagePasswordModule

/**
 * @author rival
 * @created 14/05/2020
 * @team : @minion-kevin
 *
 * For navigating to this class
 * @applink : [com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.ADD_PASSWORD]
 * @params
 *
 */

class AddPasswordActivity: BaseSimpleActivity(), HasComponent<ManagePasswordComponent> {

    override fun getScreenName(): String {
        return SCREEN_ADD_PASSWORD
    }

    override fun getComponent(): ManagePasswordComponent {
        return DaggerManagePasswordComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .managePasswordModule(ManagePasswordModule(this))
                .build()
    }

    override fun getNewFragment(): Fragment {
        return AddPasswordFragment.createInstance()
    }

    companion object {
        private const val SCREEN_ADD_PASSWORD = "Add password page"
    }
}