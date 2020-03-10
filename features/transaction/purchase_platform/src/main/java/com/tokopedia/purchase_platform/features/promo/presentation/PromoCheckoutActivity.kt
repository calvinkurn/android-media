package com.tokopedia.purchase_platform.features.promo.presentation

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_REQUEST
import com.tokopedia.purchase_platform.features.promo.data.request.PromoRequest

class PromoCheckoutActivity: BaseSimpleActivity() {

    lateinit var fragment: PromoCheckoutFragment

    override fun getNewFragment(): Fragment {
        val promoRequest = intent.getParcelableExtra(ARGS_PROMO_REQUEST) as PromoRequest
        fragment = PromoCheckoutFragment.createInstance(promoRequest)
        return fragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_promo_checkout
    }

    override fun onBackPressed() {
        fragment.onBackPressed()
    }
}