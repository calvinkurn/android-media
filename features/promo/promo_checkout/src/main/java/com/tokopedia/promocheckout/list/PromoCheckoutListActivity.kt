package com.tokopedia.promocheckout.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.constant.IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.*

open class PromoCheckoutListActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PromoCheckoutListFragment.createInstance(
                intent?.extras?.getBoolean(EXTRA_COUPON_ACTIVE, true),
                intent?.extras?.getString(EXTRA_PLATFORM, ""),
                intent?.extras?.getString(EXTRA_CATEGORY, ""),
                intent?.extras?.getString(EXTRA_CART_ID, ""),
                intent?.extras?.getString(EXTRA_TRAIN_RESERVATION_CODE, ""),
                intent?.extras?.getString(EXTRA_TRAIN_RESERVATION_ID, ""),
                intent?.extras?.getString(EXTRA_PLATFORM_PAGE, ""),
                intent?.extras?.getString(EXTRA_ADDITIONAL_STRING_DATA, "")
        )
    }

    companion object {

        fun newInstance(activity: Context, platform: String, categoryId: String, cartId: String, isCouponActive: Boolean): Intent {
            val intent = Intent(activity, PromoCheckoutListActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putString(EXTRA_PLATFORM, platform)
            bundle.putString(EXTRA_CATEGORY, categoryId)
            bundle.putString(EXTRA_CART_ID, cartId)
            intent.putExtras(bundle)
            return intent
        }

        fun newInstanceTrain(context: Context, platform: String, category: String,
                             trainReservationId: String, trainReservationCode: String, isCouponActive: Boolean): Intent {
            val intent = Intent(context, PromoCheckoutListActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putString(EXTRA_PLATFORM, platform)
            bundle.putString(EXTRA_CATEGORY, category)
            bundle.putString(EXTRA_TRAIN_RESERVATION_ID, trainReservationId)
            bundle.putString(EXTRA_TRAIN_RESERVATION_CODE, trainReservationCode)
            intent.putExtras(bundle)
            return intent
        }

        fun newInstanceWithAdditionalData(
                context: Context, additionalStringData: String, isCouponActive: Boolean
        ): Intent {
            val intent = Intent(context, PromoCheckoutListActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putString(EXTRA_PLATFORM, MARKETPLACE_STRING)
            bundle.putString(EXTRA_PLATFORM_PAGE, PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT)
            bundle.putString(EXTRA_ADDITIONAL_STRING_DATA, additionalStringData)
            intent.putExtras(bundle)
            return intent
        }
    }


}