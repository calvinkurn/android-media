package com.tokopedia.instantloan.view.contractor

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.instantloan.data.model.response.GqlFilterData
import com.tokopedia.instantloan.data.model.response.PhoneDataEntity
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity

interface OnlineLoanContractor {

    interface View : CustomerView {
        fun getAppContext(): Context?

        fun getActivityContext(): Context?

        fun setFilterDataForOnlineLoan(gqlFilterData: GqlFilterData)

        fun navigateToLoginPage()

        fun showToastMessage(message: String, duration: Int)

        fun openWebView(url: String)

        fun searchLoanOnline()

    }

    interface Presenter : CustomerPresenter<View> {
        fun isUserLoggedIn(): Boolean
        fun getFilterData()
    }
}