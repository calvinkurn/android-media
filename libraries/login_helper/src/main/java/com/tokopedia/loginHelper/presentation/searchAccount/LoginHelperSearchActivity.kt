package com.tokopedia.loginHelper.presentation.searchAccount

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.loginHelper.R
import com.tokopedia.loginHelper.presentation.searchAccount.fragment.LoginHelperSearchAccountFragment

class LoginHelperSearchActivity : BaseSimpleActivity() {

    override fun getNewFragment() = LoginHelperSearchAccountFragment.newInstance()
    override fun getLayoutRes() = R.layout.activity_login_helper
    override fun getParentViewResourceID() = R.id.container
}
