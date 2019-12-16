package com.tokopedia.instantloan.domain.subscriber

import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.instantloan.data.model.response.ResponseUserProfileStatus
import com.tokopedia.instantloan.view.contractor.DanaInstanLoanContractor
import com.tokopedia.network.utils.ErrorHandler
import rx.Subscriber
import java.lang.reflect.Type

class GetDanaInstanLoanProfileSubscriber(var presenter: DanaInstanLoanContractor.Presenter) :
        Subscriber<Map<Type, RestResponse>>() {

    override fun onNext(typeRestResponseMap: Map<Type, RestResponse>?) {

        val restResponse = typeRestResponseMap?.get(ResponseUserProfileStatus::class.java)
        val responseUserProfileStatus = restResponse!!.getData<ResponseUserProfileStatus>()
        presenter.getView().onSuccessLoanProfileStatus(responseUserProfileStatus?.userProfileLoanEntity!!)
        presenter.getView().setUserOnGoingLoanStatus(
                responseUserProfileStatus.userProfileLoanEntity!!.onGoingLoanId != 0,
                responseUserProfileStatus.userProfileLoanEntity!!.onGoingLoanId)

    }

    override fun onCompleted() {
        if (presenter.isViewAttached()) {
            presenter.getView().hideLoader()
        }
    }

    override fun onError(e: Throwable?) {
        if (presenter.isViewAttached()) {
            presenter.getView().onErrorLoanProfileStatus(
                    ErrorHandler.getErrorMessage(presenter.getView().getActivityContext(), e))
        }
    }

}
