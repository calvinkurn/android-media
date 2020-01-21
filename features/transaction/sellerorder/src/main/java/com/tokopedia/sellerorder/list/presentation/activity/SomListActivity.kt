package com.tokopedia.sellerorder.list.presentation.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.list.di.DaggerSomListComponent
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.fragment.SomListFragment

/**
 * Created by fwidjaja on 2019-08-23.
 */

// SOM = Seller Order Management
class SomListActivity: BaseSimpleActivity(), HasComponent<SomListComponent> {
    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getNewFragment(): SomListFragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras
        } else {
            bundle.putString(TAB_ACTIVE, "")
        }
        return SomListFragment.newInstance(bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.som_action_chat -> {
                onChatClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onChatClicked() {
        RouteManager.route(this, ApplinkConst.TOPCHAT_IDLESS)
    }

    override fun getComponent(): SomListComponent =
        DaggerSomListComponent.builder()
                .somComponent(SomComponentInstance.getSomComponent(application))
                .build()
}