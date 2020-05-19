package com.tokopedia.topads.dashboard.view.activity

/**
 * Created by Nathaniel on 11/22/2016.
 */

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topads.dashboard.view.fragment.TopAdsPaymentCreditFragment


class TopAdsPaymentCreditActivity : BaseSimpleActivity() {

    override fun getScreenName(): String? = null

    override fun getNewFragment() = TopAdsPaymentCreditFragment.createInstance(intent.extras)

    override fun getTagFragment(): String = TopAdsPaymentCreditFragment::class.java.simpleName
}