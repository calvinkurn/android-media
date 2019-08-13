package com.tokopedia.affiliate.feature.dashboard.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.feature.dashboard.view.fragment.CommissionDetailFragment

/**
 * @author by yoasfs on 2019-08-12
 */
class CommissionDetailActivity: BaseSimpleActivity() {

    companion object {

    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        return CommissionDetailFragment.newInstance(bundle)
    }
}