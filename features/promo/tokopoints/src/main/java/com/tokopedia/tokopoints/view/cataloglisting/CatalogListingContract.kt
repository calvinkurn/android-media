package com.tokopedia.tokopoints.view.cataloglisting

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.tokopoints.view.model.CatalogBanner
import com.tokopedia.tokopoints.view.model.CatalogFilterBase

interface CatalogListingContract {
    interface View : CustomerView {
        fun showLoader()
        fun onErrorBanners(errorMessage: String)
        fun onSuccessBanners(banners: List<CatalogBanner>)
        fun onErrorPoint(errorMessage: String)
        fun hideLoader()
        fun gotoMyCoupons()
        fun openWebView(url: String)
        fun onSuccessFilter(filters: CatalogFilterBase)
        fun onErrorFilter(errorMessage: String, hasInternet: Boolean)
        val activityContext: Context
        val appContext: Context

        fun refreshTab()
        fun onSuccessPoints(
            rewardStr: String,
            rewardValue: Int,
            membership: String,
            eggUrl: String
        )
    }

    interface Presenter {
        fun getHomePageData(
            slugCategory: String?,
            slugSubCategory: String?,
            isBannerRequire: Boolean
        )

        fun getPointData()

        val pointRangeId : Int
        val currentCategoryId : Int
        val currentSubCategoryId : Int

    }
}
