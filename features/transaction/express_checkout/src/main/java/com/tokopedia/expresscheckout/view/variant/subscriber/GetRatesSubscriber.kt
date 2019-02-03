package com.tokopedia.expresscheckout.view.variant.subscriber

import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingRecommendationData
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 08/01/19.
 */

class GetRatesSubscriber(val view: CheckoutVariantContract.View?,
                         val presenter: CheckoutVariantContract.Presenter,
                         val profileServiceId: Int,
                         val currentSpId: Int) : Subscriber<ShippingRecommendationData>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
        view?.onNeedToValidateButtonBuyVisibility()
        view?.setShippingDurationError("Toko tidak mendukung durasi pengiriman ini")
    }

    override fun onNext(ratesData: ShippingRecommendationData) {
        view?.hideLoading()
        view?.onNeedToValidateButtonBuyVisibility()
        if (ratesData.errorId != null && ratesData.errorId == ErrorProductData.ERROR_RATES_NOT_AVAILABLE) {
            showError(ratesData)
        } else if (ratesData.shippingDurationViewModels != null && ratesData.shippingDurationViewModels.size > 0) {
            // Check is service id available
            for (shippingDurationViewModel: ShippingDurationViewModel in ratesData.shippingDurationViewModels) {
                if (shippingDurationViewModel.serviceData.serviceId == profileServiceId) {
                    if (shippingDurationViewModel.serviceData.products.size > 0) {
                        if (currentSpId != 0) {
                            // Reset recommendation
                            for (product: ProductData in shippingDurationViewModel.serviceData.products) {
                                product.isRecommend = product.shipperProductId == currentSpId
                            }
                            // Reload rates data come here
                            for (product: ProductData in shippingDurationViewModel.serviceData.products) {
                                if (product.shipperProductId == currentSpId) {
                                    if (product.error.errorMessage.isNullOrEmpty()) {
                                        prepareViewModel(product, shippingDurationViewModel.serviceData, shippingDurationViewModel.shippingCourierViewModelList)
                                        return
                                    } else {
                                        view?.setShippingCourierError(product.error.errorMessage)
                                        view?.updateShippingData(product, shippingDurationViewModel.serviceData, shippingDurationViewModel.shippingCourierViewModelList)
                                        return
                                    }
                                }
                            }
                            view?.setShippingDurationError("Toko tidak mendukung durasi pengiriman ini")
                            return
                        } else {
                            // First time load rates data come here
                            for (product: ProductData in shippingDurationViewModel.serviceData.products) {
                                if (product.isRecommend) {
                                    prepareViewModel(product, shippingDurationViewModel.serviceData, shippingDurationViewModel.shippingCourierViewModelList)
                                    return
                                }
                            }
                            prepareViewModel(shippingDurationViewModel.serviceData.products[0], shippingDurationViewModel.serviceData, shippingDurationViewModel.shippingCourierViewModelList)
                            return
                        }
                    }
                }
            }
            showError(ratesData)
        } else {
            showError(ratesData)
        }
    }

    private fun showError(ratesData: ShippingRecommendationData) {
        // currentSpId 0 means first time load rates data
        if (currentSpId == 0) {
            view?.finishWithError(ratesData.errorMessage)
        } else {
            view?.setShippingDurationError(ratesData.errorMessage)
        }
    }

    private fun prepareViewModel(product: ProductData, serviceData: ServiceData, shippingCourierViewModels: MutableList<ShippingCourierViewModel>) {
        if (currentSpId == 0) {
            presenter.prepareViewModel(product)
        }
        view?.updateShippingData(product, serviceData, shippingCourierViewModels)
    }

}