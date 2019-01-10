package com.tokopedia.expresscheckout.view.variant.subscriber

import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
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
            view?.finishWithError(ratesData.errorMessage)
        } else if (ratesData.shippingDurationViewModels != null && ratesData.shippingDurationViewModels.size > 0) {
            for (shippingDurationViewModel: ShippingDurationViewModel in ratesData.shippingDurationViewModels) {
                if (shippingDurationViewModel.serviceData.serviceId == profileServiceId) {
                    if (shippingDurationViewModel.serviceData.products.size > 0) {
                        for (product: ProductData in shippingDurationViewModel.serviceData.products) {
                            if (product.isRecommend) {
                                view?.updateShippingData(product.shipperName)
                                presenter.prepareViewModel()
                                return
                            }
                        }
                        view?.updateShippingData(shippingDurationViewModel.serviceData.products[0].shipperName)
                        return
                    }
                }
            }
            view?.finishWithError(ratesData.errorMessage)
        } else {
            view?.finishWithError(ratesData.errorMessage)
        }
    }

}