package com.tokopedia.tokopoints.view.validatePin

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate

interface ValidateMerchantPinContract {
    interface View : CustomerView {
        fun showLoading()
        fun hideLoading()
        fun onSuccess(couponSwipeUpdate: CouponSwipeUpdate)
        fun onError(error: String)
        val appContext: Context
        val activityContext: Context
    }

    interface Presenter {
        fun swipeMyCoupon(partnerCode: String, pin: String)
    }
}

