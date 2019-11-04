package com.tokopedia.promocheckout.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.common.data.CHECK_PROMO_FIRST_STEP_PARAM
import com.tokopedia.promocheckout.common.data.ONE_CLICK_SHIPMENT
import com.tokopedia.promocheckout.common.data.PAGE_TRACKING
import com.tokopedia.promocheckout.common.data.PROMO_CODE
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.list.PromoCheckoutListComponentInstance
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.fragment.BasePromoCheckoutListFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListMarketplaceFragment

class PromoCheckoutListMarketplaceActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutListComponent> {

    override fun getNewFragment(): Fragment {
        return PromoCheckoutListMarketplaceFragment.createInstance(
                intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true),
                intent?.extras?.getString(PROMO_CODE, ""),
                intent?.extras?.getBoolean(ONE_CLICK_SHIPMENT, false),
                intent?.extras?.getInt(PAGE_TRACKING, 1) ?: 1,
                intent?.extras?.getParcelable(CHECK_PROMO_FIRST_STEP_PARAM) as Promo
        )
    }

    override fun getComponent(): PromoCheckoutListComponent {
        return PromoCheckoutListComponentInstance.getPromoCheckoutListComponent(application)
    }

    companion object {
        fun newInstance(activity: Context, isCouponActive: Boolean, promoCode: String, isOneClickShipment: Boolean, pageTracking: Int,
                        promo: Promo): Intent {
            val intent = Intent(activity, PromoCheckoutListMarketplaceActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putString(PROMO_CODE, promoCode)
            bundle.putBoolean(ONE_CLICK_SHIPMENT, isOneClickShipment)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            bundle.putParcelable(CHECK_PROMO_FIRST_STEP_PARAM, promo)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun onBackPressed() {

        val promocheckoutlistfragment = supportFragmentManager.fragments.get(0)
        if (promocheckoutlistfragment != null) {
            if (promocheckoutlistfragment.childFragmentManager.backStackEntryCount > 0) {
                promocheckoutlistfragment.childFragmentManager.popBackStack()
            } else
                super.onBackPressed()
        } else {
            super.onBackPressed()

        }
    }
}

