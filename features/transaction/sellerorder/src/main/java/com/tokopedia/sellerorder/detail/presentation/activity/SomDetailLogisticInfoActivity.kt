package com.tokopedia.sellerorder.detail.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.activities.BaseSomActivity
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailLogisticInfoFragment

class SomDetailLogisticInfoActivity: BaseSomActivity() {

    override fun getNewFragment(): Fragment? {
        return SomDetailLogisticInfoFragment.newInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_som_detail_logistic_info
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view_logistic_info
    }
}