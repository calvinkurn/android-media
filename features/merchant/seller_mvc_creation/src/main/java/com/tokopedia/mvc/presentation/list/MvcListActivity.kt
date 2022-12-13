package com.tokopedia.mvc.presentation.list

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.presentation.list.fragment.MvcListFragment

class MvcListActivity: BaseSimpleActivity() {

    override fun getNewFragment() = MvcListFragment()
    override fun getLayoutRes() = R.layout.smvc_activity_common
    override fun getParentViewResourceID() = R.id.container
}
