package com.tokopedia.instantloan.view.contractor

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.instantloan.data.model.response.GqlLendingDataResponse

interface InstantLoanLendingDataContractor {

    interface View : CustomerView {
        fun renderLendingData(gqlLendingDataResponse: GqlLendingDataResponse)
        fun setUserOnGoingLoanStatus(status: Boolean, id: Int)
        fun IsUserLoggedIn(): Boolean
    }

    interface Presenter : CustomerPresenter<View> {
        fun getLendingData()
        fun checkUserOnGoingLoanStatus()
    }
}
