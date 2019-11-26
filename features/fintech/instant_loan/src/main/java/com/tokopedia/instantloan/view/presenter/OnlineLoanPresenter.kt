package com.tokopedia.instantloan.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.instantloan.data.model.response.GqlFilterDataResponse
import com.tokopedia.instantloan.domain.interactor.GetFilterDataUseCase
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor
import com.tokopedia.user.session.UserSession
import rx.Subscriber
import javax.inject.Inject

class OnlineLoanPresenter @Inject
constructor(private val mGetFilterDataUseCase: GetFilterDataUseCase) :
        BaseDaggerPresenter<OnlineLoanContractor.View>(), OnlineLoanContractor.Presenter {

    @Inject
    lateinit var userSession: UserSession

    override fun attachView(view: OnlineLoanContractor.View) {
        super.attachView(view)
        getFilterData()
    }

    override fun detachView() {
        mGetFilterDataUseCase.unsubscribe()
        super.detachView()
    }

    override fun getFilterData() {

        mGetFilterDataUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onNext(graphqlResponse: GraphqlResponse?) {
                if (isViewNotAttached) {
                    return
                }
                val gqlFilterDataResponse = graphqlResponse?.getData(GqlFilterDataResponse::class.java) as GqlFilterDataResponse
                view.setFilterDataForOnlineLoan(gqlFilterDataResponse.gqlFilterData)

            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

        })

    }

    override fun isUserLoggedIn(): Boolean {
        return userSession.isLoggedIn
    }

}
