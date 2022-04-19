package com.tokopedia.promocheckout.list.view.activity

import android.app.Activity
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
import com.tokopedia.promocheckout.common.util.EXTRA_CLASHING_DATA
import com.tokopedia.promocheckout.common.util.RESULT_CLASHING
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.list.PromoCheckoutListComponentInstance
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListMarketplaceFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListMarketplaceFragment.Companion.CHECKOUT_CATALOG_DETAIL_FRAGMENT

/**
 * Old Promo List Page for Marketplace
 *
 * No longer used by minion bob & PP tribe
 */
class PromoCheckoutListMarketplaceActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutListComponent> {

    override fun getNewFragment(): Fragment {
        return PromoCheckoutListMarketplaceFragment.createInstance(
                intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true),
                intent?.extras?.getString(PROMO_CODE, ""),
                intent?.extras?.getBoolean(ONE_CLICK_SHIPMENT, false),
                intent?.extras?.getInt(PAGE_TRACKING, 1) ?: 1,
                intent?.extras?.getParcelable(CHECK_PROMO_FIRST_STEP_PARAM) ?: Promo()
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
        if (promocheckoutlistfragment != null && promocheckoutlistfragment is PromoCheckoutListMarketplaceFragment) {
            if (promocheckoutlistfragment.childFragmentManager.backStackEntryCount > 0) {
                promocheckoutlistfragment.childFragmentManager.popBackStack()
            } else
                super.onBackPressed()
        } else {
            super.onBackPressed()

        }
    }

    override fun onResume() {
        val promocheckoutlistfragment = supportFragmentManager.fragments.get(0)
        if (promocheckoutlistfragment != null && promocheckoutlistfragment is PromoCheckoutListMarketplaceFragment) {
            if (promocheckoutlistfragment.childFragmentManager.backStackEntryCount > 0) {
                promocheckoutlistfragment.childFragmentManager.findFragmentByTag(CHECKOUT_CATALOG_DETAIL_FRAGMENT)?.let { promocheckoutlistfragment.childFragmentManager.beginTransaction().remove(it).commit() }
                promocheckoutlistfragment.childFragmentManager.popBackStack()
            }
        }
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val promocheckoutlistfragment = supportFragmentManager.fragments.get(0)
        if (promocheckoutlistfragment != null && promocheckoutlistfragment is PromoCheckoutListMarketplaceFragment) {
            promocheckoutlistfragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}

