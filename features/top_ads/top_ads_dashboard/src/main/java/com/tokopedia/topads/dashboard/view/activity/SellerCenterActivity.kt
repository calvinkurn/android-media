package com.tokopedia.topads.dashboard.view.activity

import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment

/**
 * Created by hadi.putra on 16/05/18.
 */

class SellerCenterActivity : BaseSimpleActivity() {

    override fun getNewFragment() = BaseSessionWebViewFragment.newInstance(SELLER_CENTER_URL)

    companion object {
        private val SELLER_CENTER_URL = "https://seller.tokopedia.com/edu/about-topads/iklan/?source=help&medium=android"
    }
}
