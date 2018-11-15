package com.tokopedia.promocheckout.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListMarketplaceFragment

class PromoCheckoutListMarketplaceActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return PromoCheckoutListMarketplaceFragment.createInstance(
                intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true),
                intent?.extras?.getString(PromoCheckoutListMarketplaceFragment.PROMO_CODE, ""),
                intent?.extras?.getBoolean(PromoCheckoutListMarketplaceFragment.ONE_CLICK_SHIPMENT, false),
                intent?.extras?.getInt(PromoCheckoutListMarketplaceFragment.PAGE_TRACKING, 1)?:1
        )
    }

    companion object {
        fun newInstance(activity: Context, isCouponActive: Boolean, promoCode: String, isOneClickShipment: Boolean, pageTracking : Int): Intent {
            val intent = Intent(activity, PromoCheckoutListMarketplaceActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putString(PromoCheckoutListMarketplaceFragment.PROMO_CODE, promoCode)
            bundle.putBoolean(PromoCheckoutListMarketplaceFragment.ONE_CLICK_SHIPMENT, isOneClickShipment)
            bundle.putInt(PromoCheckoutListMarketplaceFragment.PAGE_TRACKING, pageTracking)
            intent.putExtras(bundle)
            return intent
        }
    }

}