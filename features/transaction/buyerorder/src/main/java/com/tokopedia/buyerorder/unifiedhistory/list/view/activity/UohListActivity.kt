package com.tokopedia.buyerorder.unifiedhistory.list.view.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment

/**
 * Created by fwidjaja on 29/06/20.
 */

// Uoh = Unified Order History
class UohListActivity: BaseSimpleActivity() {
    override fun getNewFragment(): UohListFragment? {
        return UohListFragment.newInstance()
    }
}