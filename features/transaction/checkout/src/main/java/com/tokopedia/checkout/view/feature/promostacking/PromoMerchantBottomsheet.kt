package com.tokopedia.checkout.view.feature.promostacking

import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.design.component.BottomSheets

class PromoMerchantBottomsheet : BottomSheets(), PromoMerchantListener {

    companion object {
        @JvmStatic
        fun newInstance(): PromoMerchantBottomsheet {
            val promoMerchantBottomsheet = PromoMerchantBottomsheet()
            return promoMerchantBottomsheet
        }
    }

    override fun onPromoMerchantShown() {
        println("++ PROMO MERCHANT IS SHOWN!")
        this.showsDialog = true
        updateHeight()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_promo_shop
    }

    override fun initView(view: View?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}