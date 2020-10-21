package com.tokopedia.sellerorder.detail.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailLogisticInfoFragment

class SomDetailLogisticInfoActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putParcelableArrayList(SomConsts.PARAM_LOGISTIC_INFO_ALL, arrayListOf())
        }
        return SomDetailLogisticInfoFragment.newInstance(bundle)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_som_detail_logistic_info
    }
}