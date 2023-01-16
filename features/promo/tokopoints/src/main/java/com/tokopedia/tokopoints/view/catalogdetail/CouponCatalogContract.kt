package com.tokopedia.tokopoints.view.catalogdetail

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.tokopoints.view.model.CatalogStatusItem
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity

interface CouponCatalogContract {
    interface View : CustomerView {
        fun showLoader()
        fun showError(hasInternet: Boolean)
        fun openWebView(url: String)
        fun hideLoader()
        fun populateDetail(data: CatalogsValueEntity)
        val activityContext: Context
        val appContext: Context

        fun redeemCoupon(
            cta: String?,
            code: String?,
            title: String?,
            description: String?,
            redeemMessage: String?
        )

        fun onRealCodeReFresh(realCode: String)
        fun onRealCodeReFreshError()
        fun refreshCatalog(data: CatalogStatusItem)
        fun onPreValidateError(title: String, message: String)
        fun gotoSendGiftPage(id: Int, title: String?, pointStr: String?, banner: String?)
    }

    interface Presenter {
        fun getCatalogDetail(uniqueCatalogCode: String)
        fun fetchLatestStatus(catalogsIds: List<Int>)
        fun startSendGift(id: Int, title: String?, pointStr: String?, banner: String?)
    }
}
