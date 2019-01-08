package com.tokopedia.expresscheckout.view.variant.subscriber

import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingRecommendationData
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 08/01/19.
 */

class GetRatesSubscriber(val view: CheckoutVariantContract.View?,
                         val presenter: CheckoutVariantContract.Presenter,
                         val profileServiceId: Int) : Subscriber<ShippingRecommendationData>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
    }

    override fun onNext(ratesData: ShippingRecommendationData) {
        view?.hideLoading()
        if (ratesData.errorId != null && ratesData.errorId == ErrorProductData.ERROR_RATES_NOT_AVAILABLE) {
            view?.setShippingError()
        } else if (ratesData.shippingDurationViewModels != null && ratesData.shippingDurationViewModels.size > 0) {
            for (shippingDurationViewModel: ShippingDurationViewModel in ratesData.shippingDurationViewModels) {
                if (shippingDurationViewModel.serviceData.serviceId == profileServiceId) {
                    view?.updateShippingData()
                    return
                }
            }
            view?.setShippingError()
        } else {
            view?.setShippingError()
        }
    }

}