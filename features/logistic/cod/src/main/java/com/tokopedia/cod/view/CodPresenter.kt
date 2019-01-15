package com.tokopedia.cod.view

import com.tokopedia.cod.model.CodResponse
import com.tokopedia.cod.usecase.CodConfirmUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by fajarnuha on 17/12/18.
 */
class CodPresenter @Inject constructor(val useCase: CodConfirmUseCase) : CodContract.Presenter {

    var mView: CodContract.View? = null

    override fun confirmPayment() {
        mView?.showLoading()
        useCase.execute(RequestParams.EMPTY, object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                mView?.hideLoading()
                val response = objects.getData<CodResponse>(CodResponse::class.java)
                response.checkoutCod?.data?.data?.thanksApplink?.let {
                    mView?.navigateToThankYouPage(it)
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mView?.hideLoading()
                mView?.showError(e?.message)
            }
        })
    }

    override fun attachView(view: CodContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
        useCase.unsubscribe()
    }
}