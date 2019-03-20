package com.tokopedia.promocheckout.detail.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoFirstStepParam
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailMarketplaceFragment

class PromoCheckoutDetailMarketplaceActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailMarketplaceFragment.createInstance(intent.getStringExtra(PromoCheckoutDetailMarketplaceFragment.EXTRA_KUPON_CODE),
                intent.getBooleanExtra(PromoCheckoutDetailMarketplaceFragment.EXTRA_IS_USE, false),
                intent.getBooleanExtra(PromoCheckoutDetailMarketplaceFragment.ONE_CLICK_SHIPMENT, false),
                intent.getIntExtra(PromoCheckoutDetailMarketplaceFragment.PAGE_TRACKING, 1) ?: 1,
                intent.getParcelableExtra(PromoCheckoutDetailMarketplaceFragment.CHECK_PROMO_CODE_FIRST_STEP_PARAM)
        )
    }

    companion object {
        fun createIntent(context: Context?, codeCoupon: String?, isUse: Boolean? = false, oneClickShipment: Boolean? = false, pageTracking: Int, checkPromoFirstStepParam: CheckPromoFirstStepParam?): Intent {
            val intent = Intent(context, PromoCheckoutDetailMarketplaceActivity::class.java)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.EXTRA_KUPON_CODE, codeCoupon)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.EXTRA_IS_USE, isUse)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.ONE_CLICK_SHIPMENT, oneClickShipment)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.PAGE_TRACKING, pageTracking)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.CHECK_PROMO_CODE_FIRST_STEP_PARAM, checkPromoFirstStepParam)
            return intent
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}