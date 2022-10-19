package com.tokopedia.tokopoints.view.cataloglisting

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.CustomerView

interface CatalogListItemContract {
    interface View : CustomerView {
        fun showLoader()
        fun showError()
        fun onEmptyCatalog()
        fun openWebView(url: String)
        fun hideLoader()
        val activityContext: Context
        val appContext: Context

        val currentCategoryId: Int
        val currentSubCategoryId: Int

        fun onPreValidateError(title: String, message: String)
        fun gotoSendGiftPage(id: Int, title: String, pointStr: String)
    }

    interface Presenter {
        fun fetchLatestStatus(catalogsIds: List<Int?>?)
    }
}
