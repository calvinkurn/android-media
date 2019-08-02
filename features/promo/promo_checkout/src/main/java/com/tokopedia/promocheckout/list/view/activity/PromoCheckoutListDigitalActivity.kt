package com.tokopedia.promocheckout.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListDigitalFragment

class PromoCheckoutListDigitalActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return PromoCheckoutListDigitalFragment.createInstance(
                intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true),
                intent?.extras?.getString(PromoCheckoutListDigitalFragment.EXTRA_PROMO_CODE, ""),
                intent?.extras?.getParcelable(PromoCheckoutListDigitalFragment.EXTRA_PROMO_DIGITAL_MODEL) ?: PromoDigitalModel(),
                intent?.extras?.getInt(PromoCheckoutListDigitalFragment.PAGE_TRACKING, 1) ?: 1
        )
    }

    companion object {
        fun newInstance(activity: Context, isCouponActive: Boolean, promoCode: String, promoDigitalModel: PromoDigitalModel, pageTracking: Int): Intent {
            val intent = Intent(activity, PromoCheckoutListDigitalActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putString(PromoCheckoutListDigitalFragment.EXTRA_PROMO_CODE, promoCode)
            bundle.putParcelable(PromoCheckoutListDigitalFragment.EXTRA_PROMO_DIGITAL_MODEL, promoDigitalModel)
            bundle.putInt(PromoCheckoutListDigitalFragment.PAGE_TRACKING, pageTracking)
            intent.putExtras(bundle)
            return intent
        }
    }

}