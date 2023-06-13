package com.tokopedia.tokopoints.view.couponlisting

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface CouponListingStackedContract {
    interface View : CustomerView {
        fun showLoader()
        fun hideLoader()
        fun openWebView(url: String)
        fun emptyCoupons(errors: Map<String, String>?)
        val activityContext: Context?
        val appContext: Context?
    }

    interface Presenter : CustomerPresenter<View?> {
        fun destroyView()
        fun getCoupons(categoryId: Int)
        fun setCategoryId(categoryId: Int)
        fun getCouponInStack(stackId: String?)
    }
}
