package com.tokopedia.instantloan.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.instantloan.data.model.response.GqlLendingDataResponse
import com.tokopedia.instantloan.data.model.response.ResponseUserProfileStatus
import com.tokopedia.instantloan.domain.interactor.GetLendingDataUseCase
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase
import com.tokopedia.instantloan.view.contractor.InstantLoanLendingDataContractor
import rx.Subscriber
import java.lang.reflect.Type
import javax.inject.Inject

class InstantLoanLendingDataPresenter @Inject
constructor(private val mGetLendingDataUseCase: GetLendingDataUseCase,
            private val mGetLoanProfileStatusUseCase: GetLoanProfileStatusUseCase)
    : BaseDaggerPresenter<InstantLoanLendingDataContractor.View>(), InstantLoanLendingDataContractor.Presenter {


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

    override fun getLendingData() {

        mGetLendingDataUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onNext(graphqlResponse: GraphqlResponse?) {
                if (isViewNotAttached) {
                    return
                }
                val gqlLendingDataResponse = graphqlResponse?.getData(GqlLendingDataResponse::class.java) as GqlLendingDataResponse
                view.renderLendingData(gqlLendingDataResponse)

            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

        })
    }
}
