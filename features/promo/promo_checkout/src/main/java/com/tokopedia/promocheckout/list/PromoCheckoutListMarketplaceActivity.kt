package com.tokopedia.promocheckout.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.constant.IRouterConstant

class PromoCheckoutListMarketplaceActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return PromoCheckoutListMarketplaceFragment.createInstance(
                intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true)
        )
    }

    companion object {
        fun newInstance(activity: Context,isCouponActive: Boolean): Intent {
            val intent = Intent(activity, PromoCheckoutListMarketplaceActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, isCouponActive)
            intent.putExtras(bundle)
            return intent
        }
    }

}