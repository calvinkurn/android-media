package com.tokopedia.mvc.presentation.list

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.presentation.list.fragment.MvcListFragment

class MvcListActivity: BaseSimpleActivity() {

    override fun getLayoutRes() = R.layout.smc_activity_mvc_list
    override fun getNewFragment() = MvcListFragment()
    override fun getParentViewResourceID() = R.id.parent_view
}
