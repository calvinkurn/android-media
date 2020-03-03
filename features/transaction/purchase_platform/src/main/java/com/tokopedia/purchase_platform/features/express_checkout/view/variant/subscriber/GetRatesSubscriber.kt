package com.tokopedia.purchase_platform.features.express_checkout.view.variant.subscriber

import com.tokopedia.purchase_platform.features.express_checkout.view.variant.CheckoutVariantContract
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.purchase_platform.R
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
        view?.setShippingDurationError(view.getActivityContext()?.getString(R.string.label_error_duration_not_supported)
                ?: "")
    }

    override fun onNext(ratesData: ShippingRecommendationData) {
        view?.hideLoading()
        view?.onNeedToValidateButtonBuyVisibility()
        if (ratesData.errorId != null && ratesData.errorId == ErrorProductData.ERROR_RATES_NOT_AVAILABLE) {
            showError(ratesData)
        } else if (ratesData.shippingDurationViewModels != null && ratesData.shippingDurationViewModels.size > 0) {
            var defaultProduct: ProductData? = null
            // Check is service id available
            for (shippingDurationUiModel: ShippingDurationUiModel in ratesData.shippingDurationViewModels) {
                // Reset promo data since not needed by express checkout
                shippingDurationUiModel.serviceData.isPromo = 0
                for (product: ProductData in shippingDurationUiModel.serviceData.products) {
                    product.promoCode = ""
                    if (defaultProduct == null) {
                        defaultProduct = product
                    }
                }
                if (shippingDurationUiModel.serviceData.serviceId == profileServiceId) {
                    if (shippingDurationUiModel.serviceData.products.size > 0) {
                        if (currentSpId != 0) {
                            // Reset recommendation
                            for (product: ProductData in shippingDurationUiModel.serviceData.products) {
                                product.isRecommend = product.shipperProductId == currentSpId
                            }
                            // Reload rates data come here
                            for (product: ProductData in shippingDurationUiModel.serviceData.products) {
                                if (product.shipperProductId == currentSpId) {
                                    if (product.error.errorMessage.isNullOrEmpty()) {
                                        prepareViewModel(product, shippingDurationUiModel.serviceData, shippingDurationUiModel.shippingCourierViewModelList)
                                        return
                                    } else {
                                        view?.setShippingCourierError(product.error.errorMessage)
                                        view?.updateShippingData(product, shippingDurationUiModel.serviceData, shippingDurationUiModel.shippingCourierViewModelList)
                                        return
                                    }
                                }
                            }
                            view?.setShippingDurationError(view.getActivityContext()?.getString(R.string.label_error_duration_not_supported)
                                    ?: "")
                            return
                        } else {
                            // First time load rates data come here
                            for (product: ProductData in shippingDurationUiModel.serviceData.products) {
                                if (product.isRecommend) {
                                    prepareViewModel(product, shippingDurationUiModel.serviceData, shippingDurationUiModel.shippingCourierViewModelList)
                                    return
                                }
                            }
                            prepareViewModel(shippingDurationUiModel.serviceData.products[0], shippingDurationUiModel.serviceData, shippingDurationUiModel.shippingCourierViewModelList)
                            return
                        }
                    }
                }
            }
            if (currentSpId == 0) {
                presenter.prepareViewModel(defaultProduct)
            }
            view?.setShippingDurationError(view.getActivityContext()?.getString(R.string.label_error_duration_not_supported)
                    ?: "")
        } else {
            if (currentSpId == 0) {
                view?.finishWithError("")
            } else {
                view?.setShippingDurationError(view.getActivityContext()?.getString(R.string.label_error_duration_not_supported)
                        ?: "")
            }
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

    private fun prepareViewModel(product: ProductData, serviceData: ServiceData, shippingCourierUiModels: MutableList<ShippingCourierUiModel>) {
        if (currentSpId == 0) {
            presenter.prepareViewModel(product)
        }
        view?.updateShippingData(product, serviceData, shippingCourierUiModels)
    }

}