package com.tokopedia.sellerorder.list.presentation.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.list.presentation.fragment.SomFilterFragment

/**
 * Created by fwidjaja on 2019-09-10.
 */
class SomFilterActivity: BaseSimpleActivity() {
    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun getNewFragment(): Fragment? {
        return SomFilterFragment.newInstance()
    }
}