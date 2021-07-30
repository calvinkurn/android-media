package com.tokopedia.saldodetails.contract

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter
import com.tokopedia.saldodetails.view.ui.SaldoHistoryTabItem

class SaldoHistoryContract {

    interface View : CustomerView {
        fun getContext(): Context?

        fun getStartDate(): String

        fun getEndDate(): String

//        fun getDefaultEmptyViewModel(): Visitable<*>?

        fun getActivity(): Activity?

        fun getSellerSaldoHistoryTabItem(): SaldoHistoryTabItem?

//        fun getAdapter(): SaldoDepositAdapter?

        fun getAllHistoryAdapter(): SaldoDepositAdapter?

        fun getAllSaldoHistoryTabItem(): SaldoHistoryTabItem?

        fun getBuyerHistoryAdapter(): SaldoDepositAdapter?

        fun getBuyerSaldoHistoryTabItem(): SaldoHistoryTabItem?

        fun getSellerHistoryAdapter(): SaldoDepositAdapter?

        fun getSingleTabAdapter(): SaldoDepositAdapter?

//        fun finishLoading()

        fun showErrorMessage(s: String)

        fun showInvalidDateError(s: String)

        fun getString(resId: Int): String

//        fun removeError()

        fun setActionsEnabled(isEnabled: Boolean?)

//        fun setLoading()

        fun refresh()

        fun showEmptyState()

        fun setRetry()

        fun showEmptyState(error: String)

        fun setRetry(error: String)
        fun setStartDate(date: String)
        fun setEndDate(date: String)

    }

}
