package com.tokopedia.mvc.presentation.list

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_HOME
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

    override fun onBackPressed() {
        // reinit sellerhome and promo page if all page were destroyed
        if (isTaskRoot){
            RouteManager.route(this, SELLER_HOME)
            RouteManager.route(this, CENTRALIZED_PROMO)
        }
        onBackPressedDispatcher.onBackPressed()
    }
}
