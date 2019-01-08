package com.tokopedia.cod

import com.tokopedia.cod.model.CodResponse
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
                val response = objects.getData<CodResponse>(CodResponse::class.java)
                response.data?.data?.thanksApplink?.let {
                    mView?.navigateToThankYouPage(it)
                }
            }

            override fun onCompleted() {
                mView?.hideLoading()
            }

            override fun onError(e: Throwable?) {
                mView?.showError(e?.message)
            }
        })
    }

    override fun attachView(view: CodContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }
}