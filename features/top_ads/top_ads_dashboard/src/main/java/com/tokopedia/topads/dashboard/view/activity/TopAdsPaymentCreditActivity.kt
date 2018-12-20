package com.tokopedia.topads.dashboard.view.activity

import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topads.dashboard.view.fragment.TopAdsPaymentCreditFragment

/**
 * Created by Nathaniel on 11/22/2016.
 */

class TopAdsPaymentCreditActivity : BaseSimpleActivity() {

    override fun getScreenName(): String? = null

    override fun getNewFragment() = TopAdsPaymentCreditFragment.createInstance()

    override fun getTagFragment(): String = TopAdsPaymentCreditFragment::class.java.simpleName
}