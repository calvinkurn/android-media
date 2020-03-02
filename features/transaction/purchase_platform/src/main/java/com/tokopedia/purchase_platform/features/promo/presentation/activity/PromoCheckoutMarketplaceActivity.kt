package com.tokopedia.purchase_platform.features.promo.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.fragment.PromoCheckoutMarketplaceFragment

class PromoCheckoutMarketplaceActivity: BaseSimpleActivity() {

    lateinit var fragment: PromoCheckoutMarketplaceFragment

    override fun getNewFragment(): Fragment {
        fragment = PromoCheckoutMarketplaceFragment()
        return fragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_promo_checkout
    }

    override fun onBackPressed() {
        fragment.onBackPressed()
    }
}