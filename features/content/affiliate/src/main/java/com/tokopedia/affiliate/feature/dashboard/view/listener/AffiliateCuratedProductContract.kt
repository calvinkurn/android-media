package com.tokopedia.affiliate.feature.dashboard.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CuratedProductSortViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel
import java.util.*

/**
 * Created by jegul on 2019-09-04.
 */
interface AffiliateCuratedProductContract {

    interface View : CustomerView {

        val ctx: Context?

        fun showLoading()

        fun hideLoading()

        fun onErrorGetDashboardItem(error: String)

        fun onSuccessLoadMoreDashboardItem(itemList: List<DashboardItemViewModel>, cursor: String)

        fun onGetSortOptions(sortList: List<CuratedProductSortViewModel>)

        fun onSuccessReloadSortOptions(sortList: List<CuratedProductSortViewModel>)
    }

    interface Presenter : CustomerPresenter<View> {

        fun loadCuratedProductByType(type: Int?, cursor: String, sort: Int?, startDate: Date? = null, endDate: Date? = null)

        fun loadSortOptions()

        fun reloadSortOptions(sortList: List<CuratedProductSortViewModel>, selectedId: Int?)
    }
}