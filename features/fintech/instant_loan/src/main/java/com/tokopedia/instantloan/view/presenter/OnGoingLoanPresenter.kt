package com.tokopedia.instantloan.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.instantloan.data.model.response.ResponseUserProfileStatus
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase
import com.tokopedia.instantloan.view.contractor.OnGoingLoanContractor

import java.lang.reflect.Type

import javax.inject.Inject

import rx.Subscriber

class OnGoingLoanPresenter @Inject
constructor(private val mGetLoanProfileStatusUseCase: GetLoanProfileStatusUseCase) : BaseDaggerPresenter<OnGoingLoanContractor.View>(), OnGoingLoanContractor.Presenter {

    override fun checkUserOnGoingLoanStatus() {

        mGetLoanProfileStatusUseCase.execute(object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {}

            override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                val restResponse = typeRestResponseMap[ResponseUserProfileStatus::class.java]
                val responseUserProfileStatus = restResponse!!.getData<ResponseUserProfileStatus>()
                view.setUserOnGoingLoanStatus(
                        responseUserProfileStatus.userProfileLoanEntity!!.onGoingLoanId != 0,
                        responseUserProfileStatus.userProfileLoanEntity!!.onGoingLoanId)
            }
        })
    }

}
