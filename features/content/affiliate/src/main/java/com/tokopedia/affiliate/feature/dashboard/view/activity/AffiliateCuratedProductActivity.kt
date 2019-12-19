package com.tokopedia.affiliate.feature.dashboard.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.feature.dashboard.view.fragment.AffiliateCuratedProductFragment

/**
 * Created by jegul on 2019-09-06.
 */
class AffiliateCuratedProductActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return AffiliateCuratedProductFragment.newInstance(null)
    }
}