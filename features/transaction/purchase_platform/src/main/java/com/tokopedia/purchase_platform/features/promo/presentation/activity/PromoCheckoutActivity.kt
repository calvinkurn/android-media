package com.tokopedia.purchase_platform.features.promo.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.fragment.PromoCheckoutFragment

class PromoCheckoutActivity: BaseSimpleActivity() {

    lateinit var fragment: PromoCheckoutFragment

    override fun getNewFragment(): Fragment {
        fragment = PromoCheckoutFragment()
        return fragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_promo_checkout
    }

    override fun onBackPressed() {
        fragment.onBackPressed()
    }
}