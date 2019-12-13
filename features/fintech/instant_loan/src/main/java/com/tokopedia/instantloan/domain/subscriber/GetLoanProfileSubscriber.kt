package com.tokopedia.instantloan.domain.subscriber

import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.instantloan.data.model.response.ResponseUserProfileStatus
import com.tokopedia.instantloan.view.contractor.InstantLoanLendingDataContractor
import rx.Subscriber
import java.lang.reflect.Type

class GetLoanProfileSubscriber(var presenter: InstantLoanLendingDataContractor.Presenter) :
        Subscriber<Map<Type, RestResponse>>() {

    override fun onNext(typeRestResponseMap: Map<Type, RestResponse>?) {

        if (presenter.isViewAttached()) {
            val restResponse = typeRestResponseMap?.get(ResponseUserProfileStatus::class.java)
            val responseUserProfileStatus = restResponse!!.getData<ResponseUserProfileStatus>()
            presenter.getView().setUserOnGoingLoanStatus(
                    responseUserProfileStatus.userProfileLoanEntity!!.onGoingLoanId != 0,
                    responseUserProfileStatus.userProfileLoanEntity!!.onGoingLoanId)
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {

    }
}
