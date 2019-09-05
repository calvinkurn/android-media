package com.tokopedia.affiliate.feature.dashboard.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel

/**
 * Created by jegul on 2019-09-04.
 */
interface AffiliateProductBoughtContract {

    interface View : CustomerView {

        val ctx: Context?

        fun hideLoading()

        fun onErrorGetDashboardItem(error: String)

        fun onSuccessLoadMoreDashboardItem(itemList: List<DashboardItemViewModel>, cursor: String)
    }

    interface Presenter : CustomerPresenter<View> {

        fun loadProductBoughtByType(type: Int, cursor: String)
    }
}