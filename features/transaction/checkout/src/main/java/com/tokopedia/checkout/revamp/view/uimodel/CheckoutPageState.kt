package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData

sealed class CheckoutPageState {
    object Loading: CheckoutPageState()
    class CacheExpired(val errorMessage: String): CheckoutPageState()
    class Error(val throwable: Throwable, val log: Boolean = false): CheckoutPageState()
    class CheckNoAddress(val cartShipmentAddressFormData: CartShipmentAddressFormData): CheckoutPageState()
    class NoAddress(val cartShipmentAddressFormData: CartShipmentAddressFormData, val eligible: Boolean): CheckoutPageState()
    class NoMatchedAddress(val state: Int): CheckoutPageState()
    object EmptyData : CheckoutPageState()
    class Success(val cartShipmentAddressFormData: CartShipmentAddressFormData): CheckoutPageState()
}
