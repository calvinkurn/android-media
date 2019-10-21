package com.tokopedia.promocheckout.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.list.PromoCheckoutListComponentInstance
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.fragment.BasePromoCheckoutListFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListMarketplaceFragment

class PromoCheckoutListMarketplaceActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutListComponent> {


    lateinit var promocheckoutlistfragment:PromoCheckoutListMarketplaceFragment

    override fun getNewFragment(): Fragment {
        promocheckoutlistfragment= PromoCheckoutListMarketplaceFragment.createInstance(
                intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true),
                intent?.extras?.getString(BasePromoCheckoutListFragment.EXTRA_PROMO_CODE, ""),
                intent?.extras?.getBoolean(PromoCheckoutListMarketplaceFragment.ONE_CLICK_SHIPMENT, false),
                intent?.extras?.getInt(PromoCheckoutListMarketplaceFragment.PAGE_TRACKING, 1) ?: 1,
                intent?.extras?.getParcelable(PromoCheckoutListMarketplaceFragment.CHECK_PROMO_FIRST_STEP_PARAM) as Promo
        )
        return promocheckoutlistfragment
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
            bundle.putString(BasePromoCheckoutListFragment.EXTRA_PROMO_CODE, promoCode)
            bundle.putBoolean(PromoCheckoutListMarketplaceFragment.ONE_CLICK_SHIPMENT, isOneClickShipment)
            bundle.putInt(PromoCheckoutListMarketplaceFragment.PAGE_TRACKING, pageTracking)
            bundle.putParcelable(PromoCheckoutListMarketplaceFragment.CHECK_PROMO_FIRST_STEP_PARAM, promo)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun onBackPressed() {

        val hasFragment=promocheckoutlistfragment.childFragmentManager.backStackEntryCount>0
        if(hasFragment){
            promocheckoutlistfragment.childFragmentManager.popBackStack()
        }
        else {
            super.onBackPressed()
        }
    }
}