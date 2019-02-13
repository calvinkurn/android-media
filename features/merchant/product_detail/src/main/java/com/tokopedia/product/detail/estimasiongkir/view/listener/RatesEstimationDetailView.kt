package com.tokopedia.product.detail.estimasiongkir.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.detail.estimasiongkir.data.model.RatesEstimationModel

interface RatesEstimationDetailView : CustomerView {
    fun onErrorLoadRateEstimaion(throwable: Throwable)

    fun onSuccesLoadRateEstimaion(ratesEstimationModel: RatesEstimationModel)
}
