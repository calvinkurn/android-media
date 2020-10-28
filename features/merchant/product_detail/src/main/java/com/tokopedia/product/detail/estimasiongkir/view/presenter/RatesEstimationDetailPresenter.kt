package com.tokopedia.product.detail.estimasiongkir.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.product.detail.estimasiongkir.domain.interactor.GetRateEstimationUseCase
import com.tokopedia.product.detail.estimasiongkir.view.listener.RatesEstimationDetailView
import javax.inject.Inject

class RatesEstimationDetailPresenter @Inject
constructor(private val useCase: GetRateEstimationUseCase) : BaseDaggerPresenter<RatesEstimationDetailView>() {

    override fun detachView() {
        useCase.unsubscribe()
        super.detachView()
    }
}
