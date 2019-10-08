package com.tokopedia.promocheckout.detail.view.presenter

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.promocheckout.detail.model.detailmodel.HachikoCatalogDetail

interface CheckoutCatalogDetailContract {

    interface View : CustomerView {

        fun getActivityContext(): Context?

        fun getAppContext(): Context?

        fun showLoader()

        fun hideLoader()

        fun populateDetail(data: HachikoCatalogDetail)

        fun showCouponDetail(cta: String?, code: String?, title: String?)

        fun showValidationMessageDialog(item: HachikoCatalogDetail, title: String, message: String, resCode: Int)

        fun onSuccessPoints(point: String)

    }

    interface Presenter : CustomerPresenter<View> {
        fun destroyView()

        fun getCatalogDetail(uniqueCatalogCode: String, catalog_id: Int)

    }
}
