package com.tokopedia.logisticcart.cod.view

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticcart.cod.model.CodResponse
import com.tokopedia.logisticcart.cod.usecase.CodConfirmUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class CodPresenter @Inject constructor(val useCase: CodConfirmUseCase) : CodContract.Presenter {

    var mView: CodContract.View? = null

    override fun confirmPayment() {
        mView?.showLoading()
        useCase.execute(RequestParams.EMPTY, object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                mView?.hideLoading()
                val response = objects.getData<CodResponse>(CodResponse::class.java)
                response?.checkoutCod?.data?.data?.thanksApplink?.let {
                    mView?.sendEventEECod()
                    mView?.navigateToThankYouPage(it)
                } ?: mView?.showError(objects.getError(CodResponse::class.java)[0].message)
            }

            override fun onCompleted() {
                //no-op
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