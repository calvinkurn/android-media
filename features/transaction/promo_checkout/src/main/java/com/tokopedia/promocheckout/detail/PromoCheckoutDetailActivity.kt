package com.tokopedia.promocheckout.detail

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class PromoCheckoutDetailActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailFragment.createInstance()
    }
}