package com.tokopedia.loginHelper.presentation.accountSettings

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.loginHelper.R

class LoginHelperAccountSettingsActivity : BaseSimpleActivity() {
    override fun getNewFragment() = null
    override fun getLayoutRes() = R.layout.activity_login_helper
    override fun getParentViewResourceID() = R.id.container
}
