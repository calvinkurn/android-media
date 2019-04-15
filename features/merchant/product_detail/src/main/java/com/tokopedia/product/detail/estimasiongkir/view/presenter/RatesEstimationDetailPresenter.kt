package com.tokopedia.product.detail.estimasiongkir.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.product.detail.estimasiongkir.data.model.RatesEstimationModel
import com.tokopedia.product.detail.estimasiongkir.domain.interactor.GetRateEstimationUseCase
import com.tokopedia.product.detail.estimasiongkir.view.listener.RatesEstimationDetailView

import javax.inject.Inject

import rx.Subscriber

class RatesEstimationDetailPresenter @Inject
constructor(private val useCase: GetRateEstimationUseCase) : BaseDaggerPresenter<RatesEstimationDetailView>() {

    override fun detachView() {
        useCase.unsubscribe()
        super.detachView()
    }

    fun getCostEstimation(rawQuery: String, productWeight: Float, shopDomain: String = "") {
        useCase.execute(GetRateEstimationUseCase.createRequestParams(rawQuery, productWeight, shopDomain),
                object : Subscriber<RatesEstimationModel.Data>() {
                    override fun onCompleted() {}

                    override fun onError(throwable: Throwable) {
                        throwable.printStackTrace()
                        view?.onErrorLoadRateEstimaion(throwable)
                    }

                    override fun onNext(data: RatesEstimationModel.Data) {
                        val rateEstimation = data.ratesEstimation.firstOrNull() ?: return
                        view?.onSuccesLoadRateEstimaion(rateEstimation, data.isBlackbox)
                    }
                })
    }
}
