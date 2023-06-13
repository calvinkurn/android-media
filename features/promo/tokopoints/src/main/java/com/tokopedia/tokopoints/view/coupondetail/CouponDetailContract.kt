package com.tokopedia.tokopoints.view.coupondetail

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate
import com.tokopedia.tokopoints.view.model.CouponValueEntity

interface CouponDetailContract {
    interface View : CustomerView {
        fun showLoader()
        fun showError(networkError: Boolean)
        fun openWebView(url: String)
        fun hideLoader()
        fun populateDetail(data: CouponValueEntity)
        val activityContext: Context?
        val appContext: Context?

        fun showRedeemFullError(item: CatalogsValueEntity, title: String, desc: String)
        fun onRealCodeReFresh(realCode: String?)
        fun onRealCodeReFreshError()
        fun onSwipeResponse(data: CouponSwipeUpdate, qrCodeLink: String, barCodeLink: String)
        fun onSwipeError(errorMessage: String)
    }

    interface Presenter : CustomerPresenter<View?> {
        fun destroyView()
        fun getCouponDetail(uniqueCouponCode: String?)
        fun reFetchRealCode(uniqueCatalogCode: String)
        fun swipeMyCoupon(partnerCode: String?, pin: String?)
    }
}
