package com.tokopedia.mvc.presentation.list

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.mvc.R
import com.tokopedia.mvc.presentation.list.fragment.MvcListFragment

class MvcListActivity: BaseSimpleActivity() {

    private fun getStatusFilterFromUri(): String {
        val data = RouteManager.getIntent(this, intent.data.toString()).data
        return data?.pathSegments?.lastOrNull().orEmpty()
    }

    override fun getNewFragment() = MvcListFragment.newInstance(getStatusFilterFromUri())
    override fun getLayoutRes() = R.layout.smvc_activity_common
    override fun getParentViewResourceID() = R.id.container
}
