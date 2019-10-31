package com.tokopedia.promocheckout.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.list.PromoCheckoutListComponentInstance
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.fragment.BasePromoCheckoutListFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListDigitalFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListFlightFragment

class PromoCheckoutListFlightActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutListComponent> {

    override fun getNewFragment(): Fragment {
        return PromoCheckoutListFlightFragment.createInstance(
                intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true),
                intent?.extras?.getString(BasePromoCheckoutListFragment.EXTRA_PROMO_CODE, ""),
                intent?.extras?.getString(PromoCheckoutListFlightFragment.EXTRA_CART_ID, ""),
                intent?.extras?.getInt(BasePromoCheckoutListFragment.PAGE_TRACKING, 1) ?: 1
        )
    }

    override fun getComponent(): PromoCheckoutListComponent {
        return PromoCheckoutListComponentInstance.getPromoCheckoutListComponent(application)
    }

    companion object {
        fun newInstance(activity: Context, isCouponActive: Boolean, promoCode: String, cartID: String, pageTracking: Int): Intent {
            val intent = Intent(activity, PromoCheckoutListFlightActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putString(BasePromoCheckoutListFragment.EXTRA_PROMO_CODE, promoCode)
            bundle.putString(PromoCheckoutListFlightFragment.EXTRA_CART_ID, cartID)
            bundle.putInt(BasePromoCheckoutListFragment.PAGE_TRACKING, pageTracking)
            intent.putExtras(bundle)
            return intent
        }
    }

}