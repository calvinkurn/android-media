package com.tokopedia.saldodetails.feature_saldo_hold_info

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.saldodetails.feature_saldo_hold_info.response.SaldoHoldResponse
import rx.Subscriber

class SaldoHoldInfoPresenter(val getHoldInfoUsecase: GetHoldInfoUsecase) : BaseDaggerPresenter<SaldoHoldInfoContract.View>(), SaldoHoldInfoContract.Presenter {

    override fun detachView() {
        getHoldInfoUsecase.unsubscribe()
    }

    override fun getSaldoHoldInfo() {

        getHoldInfoUsecase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                view.showErrorView()
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