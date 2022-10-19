package com.tokopedia.tokopoints.view.couponlisting

import android.content.Context
import androidx.annotation.RawRes
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.tokopoints.view.model.CouponFilterItem

interface StackedCouponActivityContract {
    interface View : CustomerView {
        fun showLoading()
        fun hideLoading()
        fun onSuccess(data: List<CouponFilterItem>)
        fun onError(error: String, hasInternet: Boolean)
        val appContext: Context?
        val activityContext: Context?

        fun getStringRaw(@RawRes id: Int): String?
    }

    interface Presenter : CustomerPresenter<View?> {
        fun destroyView()
        fun getFilter(slug: String?)
    }
}
