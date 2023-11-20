package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData

sealed class CheckoutPageState {
    object Loading : CheckoutPageState()
    data class CacheExpired(val errorMessage: String) : CheckoutPageState()
    data class Error(val throwable: Throwable) : CheckoutPageState()
    data class NoAddress(val cartShipmentAddressFormData: CartShipmentAddressFormData) : CheckoutPageState()
    data class NoMatchedAddress(val state: Int) : CheckoutPageState()
    object EmptyData : CheckoutPageState()
    data class Success(val cartShipmentAddressFormData: CartShipmentAddressFormData) : CheckoutPageState()
    object Normal : CheckoutPageState()
    data class ScrollTo(val index: Int) : CheckoutPageState()
    data class PriceValidation(val priceValidationData: PriceValidationData) : CheckoutPageState()
    data class Prompt(val prompt: com.tokopedia.checkout.domain.model.checkout.Prompt) : CheckoutPageState()
    object EpharmacyCoachMark : CheckoutPageState()
    data class AkamaiRatesError(val message: String) : CheckoutPageState()
}

data class CheckoutPageToaster(
    val toasterType: Int,
    val toasterMessage: String = "",
    val throwable: Throwable? = null
)
