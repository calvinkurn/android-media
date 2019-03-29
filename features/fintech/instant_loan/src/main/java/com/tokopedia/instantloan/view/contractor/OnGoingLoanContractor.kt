package com.tokopedia.instantloan.view.contractor

import android.content.Context

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface OnGoingLoanContractor {

    interface View : CustomerView {
        fun setUserOnGoingLoanStatus(status: Boolean, id: Int)
    }

    interface Presenter : CustomerPresenter<OnGoingLoanContractor.View> {
        fun checkUserOnGoingLoanStatus()
    }

}
