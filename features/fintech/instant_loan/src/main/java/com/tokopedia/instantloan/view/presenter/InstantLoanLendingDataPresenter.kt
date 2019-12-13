package com.tokopedia.instantloan.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.instantloan.domain.interactor.GetLendingDataUseCase
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase
import com.tokopedia.instantloan.domain.subscriber.GetLendingDataSubscriber
import com.tokopedia.instantloan.domain.subscriber.GetLoanProfileSubscriber
import com.tokopedia.instantloan.view.contractor.InstantLoanLendingDataContractor
import javax.inject.Inject

class InstantLoanLendingDataPresenter @Inject
constructor(private val mGetLendingDataUseCase: GetLendingDataUseCase,
            private val mGetLoanProfileStatusUseCase: GetLoanProfileStatusUseCase)
    : BaseDaggerPresenter<InstantLoanLendingDataContractor.View>(), InstantLoanLendingDataContractor.Presenter {


    var getLendingDataSubscriber = GetLendingDataSubscriber(this)
    var getLoanProfileSubscriber = GetLoanProfileSubscriber(this)

    override fun detachView() {
        super.detachView()
        mGetLendingDataUseCase.unsubscribe()
        mGetLoanProfileStatusUseCase.unsubscribe()

    }
    override fun attachView(view: InstantLoanLendingDataContractor.View?) {
        super.attachView(view)
        getLendingData()
        if (view?.IsUserLoggedIn()!!) {
            checkUserOnGoingLoanStatus()
        }
    }

    private fun checkUserOnGoingLoanStatus() {
        mGetLoanProfileStatusUseCase.execute(getLoanProfileSubscriber)
    }

    private fun getLendingData() {
        mGetLendingDataUseCase.execute(getLendingDataSubscriber)
    }
}
