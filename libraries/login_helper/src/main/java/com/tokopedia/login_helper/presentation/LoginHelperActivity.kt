package com.tokopedia.login_helper.presentation

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.login_helper.R
import com.tokopedia.login_helper.presentation.fragment.LoginHelperFragment

class LoginHelperActivity: BaseSimpleActivity() {
    override fun getNewFragment() = LoginHelperFragment.newInstance()
    override fun getLayoutRes() = R.layout.activity_login_helper
    override fun getParentViewResourceID() = R.id.container
}
