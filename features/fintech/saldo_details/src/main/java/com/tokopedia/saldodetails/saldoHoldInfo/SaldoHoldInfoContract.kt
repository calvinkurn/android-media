package com.tokopedia.saldodetails.saldoHoldInfo

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.saldodetails.saldoHoldInfo.response.SaldoHoldDepositHistory

interface SaldoHoldInfoContract {

    interface View : CustomerView {
        fun renderSaldoHoldInfo(saldoHoldDepositHistory: SaldoHoldDepositHistory?)
        fun showErrorView()
    }

    interface Presenter : CustomerPresenter<View> {
        fun getSaldoHoldInfo()
    }
}