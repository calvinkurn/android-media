package com.tokopedia.saldodetails.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldDepositHistory

interface SaldoHoldInfoContract {

    interface View : CustomerView {
        fun renderSaldoHoldInfo(saldoHoldDepositHistory: SaldoHoldDepositHistory?)
    }

    interface Presenter : CustomerPresenter<SaldoHoldInfoContract.View> {
        fun getSaldoHoldInfo()
    }
}