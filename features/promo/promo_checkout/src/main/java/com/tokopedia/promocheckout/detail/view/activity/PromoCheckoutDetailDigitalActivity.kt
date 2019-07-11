package com.tokopedia.promocheckout.detail.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailMarketplaceFragment

class PromoCheckoutDetailDigitalActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailMarketplaceFragment.createInstance(intent.getStringExtra(PromoCheckoutDetailMarketplaceFragment.EXTRA_KUPON_CODE),
                intent.getBooleanExtra(PromoCheckoutDetailMarketplaceFragment.EXTRA_IS_USE, false),
                intent.getBooleanExtra(PromoCheckoutDetailMarketplaceFragment.ONE_CLICK_SHIPMENT, false),
                intent.getIntExtra(PromoCheckoutDetailMarketplaceFragment.PAGE_TRACKING, 1) ?: 1,
                intent.getParcelableExtra(PromoCheckoutDetailMarketplaceFragment.CHECK_PROMO_CODE_FIRST_STEP_PARAM)
        )
    }

    companion object {
        fun createIntent(context: Context?, codeCoupon: String?, isUse: Boolean? = false, oneClickShipment: Boolean? = false, pageTracking: Int, promo: Promo?): Intent {
            val intent = Intent(context, PromoCheckoutDetailDigitalActivity::class.java)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.EXTRA_KUPON_CODE, codeCoupon)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.EXTRA_IS_USE, isUse)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.ONE_CLICK_SHIPMENT, oneClickShipment)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.PAGE_TRACKING, pageTracking)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.CHECK_PROMO_CODE_FIRST_STEP_PARAM, promo)
            return intent
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}