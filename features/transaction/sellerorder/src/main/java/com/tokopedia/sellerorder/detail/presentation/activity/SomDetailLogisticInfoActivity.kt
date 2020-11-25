package com.tokopedia.sellerorder.detail.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailLogisticInfoFragment

class SomDetailLogisticInfoActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return SomDetailLogisticInfoFragment.newInstance()
    }

    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple
}