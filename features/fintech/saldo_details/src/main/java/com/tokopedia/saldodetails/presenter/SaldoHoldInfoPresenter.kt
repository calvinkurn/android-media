package com.tokopedia.saldodetails.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.saldodetails.contract.SaldoHoldInfoContract
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldResponse
import com.tokopedia.saldodetails.usecase.GetHoldInfoUsecase
import rx.Subscriber

class SaldoHoldInfoPresenter (val getHoldInfoUsecase: GetHoldInfoUsecase) : BaseDaggerPresenter<SaldoHoldInfoContract.View>(), SaldoHoldInfoContract.Presenter {

    override fun detachView() {
        getHoldInfoUsecase.unsubscribe()
    }

    override fun getSaldoHoldInfo() {

        getHoldInfoUsecase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: GraphqlResponse?) {
                val dataDetailCheckoutPromo = t?.getData<SaldoHoldResponse>(SaldoHoldResponse::class.java)

                dataDetailCheckoutPromo?.let {
                    view.renderSaldoHoldInfo(it.saldoHoldDepositHistory)
                }
            }
        })
    }

}