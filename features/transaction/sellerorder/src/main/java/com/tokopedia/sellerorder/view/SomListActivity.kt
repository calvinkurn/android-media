package com.tokopedia.sellerorder.view

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.R

/**
 * Created by fwidjaja on 2019-08-23.
 */
class SomListActivity: BaseSimpleActivity() {
    override fun getLayoutRes(): Int = R.layout.activity_som_list

    override fun getNewFragment(): SomListFragment? {
        var fragment: SomListFragment? = null
        if (intent.extras != null) {
            // val bundle = intent.extras
            fragment = SomListFragment.newInstance()
        }
        return fragment
    }
}