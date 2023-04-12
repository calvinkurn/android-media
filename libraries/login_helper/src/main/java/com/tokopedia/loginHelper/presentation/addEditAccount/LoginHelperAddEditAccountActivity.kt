package com.tokopedia.loginHelper.presentation.addEditAccount

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.loginHelper.R

class LoginHelperAddEditAccountActivity : BaseSimpleActivity() {
    override fun getNewFragment() = null
    override fun getLayoutRes() = R.layout.activity_login_helper
    override fun getParentViewResourceID() = R.id.container
}
