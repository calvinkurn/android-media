package com.tokopedia.pdpCheckout.testing.checkout.robot

import com.tokopedia.pdpCheckout.testing.R

object CheckoutPageMocks {

    const val SHIPMENT_ADDRESS_FORM_KEY = "shipmentAddressFormV4"

    val SHIPMENT_ADDRESS_FORM_DEFAULT_RESPONSE = R.raw.saf_bundle_analytics_default_response

    const val SAVE_SHIPMENT_KEY = "save_shipment"
    val SAVE_SHIPMENT_DEFAULT_RESPONSE = R.raw.save_shipment_default_response

    const val RATES_V3_KEY = "ratesV3"
    val RATES_V3_DEFAULT_RESPONSE = R.raw.ratesv3_analytics_default_response

    const val VALIDATE_USE_KEY = "validate_use_promo_revamp"
    val VALIDATE_USE_DEFAULT_RESPONSE = R.raw.validate_use_analytics_default_response

    const val CHECKOUT_KEY = "checkout"
    val CHECKOUT_DEFAULT_RESPONSE = R.raw.checkout_analytics_default_response
}
