package com.tokopedia.purchase_platform.features.promo.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.purchase_platform.features.promo.presentation.fragment.PromoCheckoutMarketplaceFragment

class PromoCheckoutMarketplaceActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return PromoCheckoutMarketplaceFragment()
    }

}