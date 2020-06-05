package com.tokopedia.managepassword.changepassword.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.managepassword.changepassword.view.fragment.ChangePasswordFragment
import com.tokopedia.managepassword.di.DaggerManagePasswordComponent
import com.tokopedia.managepassword.di.ManagePasswordComponent
import com.tokopedia.managepassword.di.module.ManagePasswordModule

/**
 * @author rival
 * @created 20/02/2020
 *
 * @team: @minion-kevin
 *
 * For navigate : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_PASSWORD]
 * please pass :
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL]
 * */

class ChangePasswordActivity : BaseSimpleActivity(), HasComponent<ManagePasswordComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ChangePasswordFragment()
    }

    override fun getScreenName(): String {
        return SCREEN_ADD_PASSWORD
    }

    override fun getComponent(): ManagePasswordComponent {
        return DaggerManagePasswordComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .managePasswordModule(ManagePasswordModule(this))
                .build()
    }

    companion object {
        private const val SCREEN_ADD_PASSWORD = "Change password page"
    }
}
