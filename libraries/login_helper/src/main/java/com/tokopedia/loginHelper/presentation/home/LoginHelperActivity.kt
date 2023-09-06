package com.tokopedia.loginHelper.presentation.home

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.loginHelper.R
import com.tokopedia.loginHelper.presentation.home.fragment.LoginHelperFragment

class LoginHelperActivity : BaseSimpleActivity() {
    override fun getNewFragment() = LoginHelperFragment.newInstance()
    override fun getLayoutRes() = R.layout.activity_login_helper
    override fun getParentViewResourceID() = R.id.container
}
