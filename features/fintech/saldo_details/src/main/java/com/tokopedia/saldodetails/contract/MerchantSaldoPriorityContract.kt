package com.tokopedia.saldodetails.contract

import android.content.Context

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface MerchantSaldoPriorityContract {

    interface View : CustomerView {

        val context: Context
        fun showProgressLoading()

        fun hideProgressLoading()

        fun onSaldoStatusUpdateError(errorMessage: String)

        fun onSaldoStatusUpdateSuccess(newState: Boolean)
    }

    interface Presenter : CustomerPresenter<View> {
        fun onDestroyView()

        fun updateSellerSaldoStatus(value: Boolean)
    }
}


