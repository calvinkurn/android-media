package com.tokopedia.instantloan.view.contractor

import android.content.Context

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.instantloan.data.model.response.PhoneDataEntity
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity

interface InstantLoanContractor {

    interface View : CustomerView {
        fun getAppContext(): Context?

        fun getActivityContext(): Context?

        fun onSuccessLoanProfileStatus(status: UserProfileLoanEntity)

        fun setUserOnGoingLoanStatus(status: Boolean, loanId: Int)

        fun onErrorLoanProfileStatus(onErrorLoanProfileStatus: String)

        fun onSuccessPhoneDataUploaded(data: PhoneDataEntity)

        fun onErrorPhoneDataUploaded(errorMessage: String)

        fun navigateToLoginPage()

        fun startIntroSlider()

        fun showToastMessage(message: String, duration: Int)

        fun openWebView(url: String)

        fun searchLoanOnline()

        fun showLoader()

        fun hideLoader()

        fun showLoaderIntroDialog()

        fun hideLoaderIntroDialog()

        fun hideIntroDialog()
    }

    interface Presenter : CustomerPresenter<View> {

        fun isUserLoggedIn(): Boolean
        fun initialize()

        fun getLoanProfileStatus()

        fun postPhoneData(userId: String)

        fun startDataCollection()
    }
}
