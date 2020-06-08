package com.tokopedia.promocheckoutmarketplace.presentation

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.purchase_platform.common.constant.ARGS_BBO_PROMO_CODES
import com.tokopedia.purchase_platform.common.constant.ARGS_PAGE_SOURCE
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_REQUEST
import com.tokopedia.purchase_platform.common.constant.ARGS_VALIDATE_USE_REQUEST
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest

class PromoCheckoutActivity : BaseSimpleActivity() {

    lateinit var fragment: PromoCheckoutFragment

    override fun getNewFragment(): Fragment {
        val pageSource = intent.getIntExtra(ARGS_PAGE_SOURCE, 0)
        val promoRequest = intent.getParcelableExtra(ARGS_PROMO_REQUEST) as PromoRequest
        val validateUseRequest = intent.getParcelableExtra(ARGS_VALIDATE_USE_REQUEST) as ValidateUsePromoRequest
        val bboPromoCodes = intent.getStringArrayListExtra(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
        fragment = PromoCheckoutFragment.createInstance(pageSource, promoRequest, validateUseRequest, bboPromoCodes ?: ArrayList())
        return fragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_promo_checkout
    }

    override fun onBackPressed() {
        if (::fragment.isInitialized) {
            fragment.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}