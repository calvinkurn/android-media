package com.tokopedia.affiliate.feature.dashboard.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.ShareableByMeProfileViewModel
import com.tokopedia.calendar.Legend
import java.util.*

/**
 * Created by jegul on 2019-09-02.
 */
interface AffiliateDashboardContract {

    interface View : CustomerView {

        val ctx: Context?

        fun showLoading()

        fun hideLoading()

        fun onSuccessGetDashboardItem(header: DashboardHeaderViewModel, byMeHeader: ShareableByMeProfileViewModel)

        fun onErrorCheckAffiliate(error: String)

        fun onSuccessCheckAffiliate(isAffiliate: Boolean)

        fun onUserNotLoggedIn()

        fun onGetHolidayList(legendList: List<Legend>)

        fun onErrorGetHoliday(error: Throwable)

        fun onErrorGetDashboardItem(error: String)
    }

    interface Presenter : CustomerPresenter<View> {

        fun loadDashboardDetail(startDate: Date? = null, endDate: Date? = null)

        fun checkAffiliate()

        fun loadHolidayList()
    }
}