package com.tokopedia.promocheckout.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.list.PromoCheckoutListComponentInstance
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.fragment.BasePromoCheckoutListFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListDealsFragment

class PromoCheckoutListDealsActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutListComponent> {

    override fun getNewFragment(): Fragment? {
        return PromoCheckoutListDealsFragment.createInstance(
                intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true),
                intent?.extras?.getString(BasePromoCheckoutListFragment.EXTRA_PROMO_CODE, ""),
                intent?.extras?.getInt(PromoCheckoutListDealsFragment.EXTRA_CATEGORY_ID, 1) ?: 1,
                intent?.extras?.getInt(BasePromoCheckoutListFragment.PAGE_TRACKING, 1) ?: 1,
                intent?.extras?.getString(PromoCheckoutListDealsFragment.EXTRA_PRODUCTID, ""),
                intent?.extras?.getString(PromoCheckoutListDealsFragment.EXTRA_CHECKOUT_DATA, ""))
    }

    override fun getComponent(): PromoCheckoutListComponent {
        return PromoCheckoutListComponentInstance.getPromoCheckoutListComponent(application)
    }

    companion object {
        fun newInstance(activity: Context, isCouponActive: Boolean, platform: String, categoryId: String, pageTracking: Int): Intent {
            val intent = Intent(activity, PromoCheckoutListDealsActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_PLATFORM, platform)
            bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORYID, categoryId)
            bundle.putInt(BasePromoCheckoutListFragment.PAGE_TRACKING, pageTracking)
            intent.putExtras(bundle)
            return intent
        }
    }
}