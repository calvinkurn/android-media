package com.tokopedia.promocheckoutmarketplace.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.localizationchooseaddress.util.ChosenAddress
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest

class PromoCheckoutActivity : BaseSimpleActivity() {

    lateinit var fragment: PromoCheckoutFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.gone()
    }

    override fun getNewFragment(): Fragment {
        val pageSource = intent.getIntExtra(ARGS_PAGE_SOURCE, 0)
        val promoRequest = intent.getParcelableExtra(ARGS_PROMO_REQUEST) as PromoRequest
        val validateUseRequest = intent.getParcelableExtra(ARGS_VALIDATE_USE_REQUEST) as ValidateUsePromoRequest
        val bboPromoCodes = intent.getStringArrayListExtra(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
        val promoMvcLockCourierFlow = intent.getBooleanExtra(ARGS_PROMO_MVC_LOCK_COURIER_FLOW, false)
        val chosenAddress: ChosenAddress? = intent.getParcelableExtra(ARGS_CHOSEN_ADDRESS)
        fragment = PromoCheckoutFragment.createInstance(
                pageSource,
                promoRequest,
                validateUseRequest,
                bboPromoCodes ?: ArrayList(),
                promoMvcLockCourierFlow,
                chosenAddress)
        return fragment
    }

    override fun getLayoutRes(): Int {
        return com.tokopedia.abstraction.R.layout.activity_base_simple
    }

    override fun onBackPressed() {
        if (::fragment.isInitialized) {
            fragment.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}