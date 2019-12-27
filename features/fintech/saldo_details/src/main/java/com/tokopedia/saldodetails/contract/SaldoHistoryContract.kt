package com.tokopedia.saldodetails.contract

import android.app.Activity
import android.content.Context

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter
import com.tokopedia.date.util.SaldoDatePickerUtil
import com.tokopedia.saldodetails.view.ui.SaldoHistoryTabItem

class SaldoHistoryContract {

    interface View : CustomerView {
        val context: Context

        var startDate: String

        var endDate: String

        val defaultEmptyViewModel: Visitable<*>

        val activity: Activity

        val sellerSaldoHistoryTabItem: SaldoHistoryTabItem

        val adapter: SaldoDepositAdapter

        val singleHistoryTabItem: SaldoHistoryTabItem

        val allHistoryAdapter: SaldoDepositAdapter

        val allSaldoHistoryTabItem: SaldoHistoryTabItem

        val buyerHistoryAdapter: SaldoDepositAdapter

        val buyerSaldoHistoryTabItem: SaldoHistoryTabItem

        val sellerHistoryAdapter: SaldoDepositAdapter

        val singleTabAdapter: SaldoDepositAdapter

        fun finishLoading()

        fun showErrorMessage(s: String)

        fun showInvalidDateError(s: String)

        fun getString(resId: Int): String

        fun removeError()

        fun setActionsEnabled(isEnabled: Boolean?)

        fun setLoading()

        fun refresh()

        fun showEmptyState()

        fun setRetry()

        fun showEmptyState(error: String)

        fun setRetry(error: String)

    }

    interface Presenter : CustomerPresenter<SaldoHistoryContract.View> {
        fun setFirstDateParameter()

        fun onSearchClicked()

        fun onEndDateClicked(datePicker: SaldoDatePickerUtil)

        fun onStartDateClicked(datePicker: SaldoDatePickerUtil)

        fun loadMore(lastItemPosition: Int, visibleItem: Int)

        fun getSummaryDeposit()

        fun onRefresh()
    }
}
