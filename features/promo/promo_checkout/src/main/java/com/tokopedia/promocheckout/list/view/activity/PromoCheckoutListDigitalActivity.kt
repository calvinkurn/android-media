package com.tokopedia.promocheckout.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListDigitalFragment

class PromoCheckoutListDigitalActivity : BaseSimpleActivity(){
    override fun getNewFragment(): Fragment {
        return PromoCheckoutListDigitalFragment.createInstance(
                intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true),
                intent?.extras?.getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY, ""),
                intent?.extras?.getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CART_ID, ""),
                intent?.extras?.getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_TRAIN_RESERVATION_CODE, ""),
                intent?.extras?.getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_TRAIN_RESERVATION_ID, "")
        )
    }

    companion object {
        fun newInstanceDigital(activity: Context, categoryId: String, isCouponActive: Boolean): Intent {
            return createInstance(activity, categoryId, isCouponActive = isCouponActive)
        }

        fun newInstanceFlight(activity: Context, categoryId: String, cartId: String, isCouponActive: Boolean): Intent {
            return createInstance(activity, categoryId, cartId, isCouponActive)
        }

        fun newInstanceTrain(context: Context, category: String,
                             trainReservationId: String, trainReservationCode: String, isCouponActive: Boolean): Intent {
            return createInstance(context, category, isCouponActive = isCouponActive, trainReservationId = trainReservationId,
                    trainReservationCode = trainReservationCode)
        }

        fun createInstance(activity: Context, categoryId: String, cartId: String = "", isCouponActive: Boolean,
                           trainReservationId: String ="", trainReservationCode: String = "") : Intent{
            val intent = Intent(activity, PromoCheckoutListDigitalActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CATEGORY, categoryId)
            bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_TRAIN_RESERVATION_ID, trainReservationId)
            bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_TRAIN_RESERVATION_CODE, trainReservationCode)
            bundle.putString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_CART_ID, cartId)
            intent.putExtras(bundle)
            return intent
        }

    }


}