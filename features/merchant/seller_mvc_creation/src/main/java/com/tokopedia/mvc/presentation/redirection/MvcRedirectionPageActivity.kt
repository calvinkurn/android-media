package com.tokopedia.mvc.presentation.redirection

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R

class MvcRedirectionPageActivity: BaseSimpleActivity() {

    override fun getNewFragment() = MvcRedirectionPageFragment()
    override fun getLayoutRes() = R.layout.smvc_activity_common
    override fun getParentViewResourceID() = R.id.container
}
