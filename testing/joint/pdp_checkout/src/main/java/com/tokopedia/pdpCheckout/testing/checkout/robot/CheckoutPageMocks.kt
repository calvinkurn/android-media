package com.tokopedia.pdpCheckout.testing.checkout.robot

import com.tokopedia.pdpCheckout.testing.R

object CheckoutPageMocks {

    const val SHIPMENT_ADDRESS_FORM_KEY = "shipment_address_form_v3"

    val SHIPMENT_ADDRESS_FORM_DEFAULT_RESPONSE = R.raw.saf_analytics_default_response
    val SHIPMENT_ADDRESS_FORM_PROMO_RESPONSE = R.raw.saf_analytics_promo_response
    val SHIPMENT_ADDRESS_FORM_TOKONOW_RESPONSE = R.raw.saf_tokonow_default_response
    val SHIPMENT_ADDRESS_FORM_TOKONOW_WITH_FAILED_DEFAULT_DURATION_RESPONSE = R.raw.saf_tokonow_with_failed_default_duration_response

    const val SAVE_SHIPMENT_KEY = "save_shipment"
    val SAVE_SHIPMENT_DEFAULT_RESPONSE = R.raw.save_shipment_default_response
    
    const val RATES_V3_KEY = "ratesV3"
    val RATES_V3_DEFAULT_RESPONSE = R.raw.ratesv3_analytics_default_response
    val RATES_V3_TOKONOW_RESPONSE = R.raw.ratesv3_tokonow_default_response
    val RATES_V3_TOKONOW_WITH_ADDITIONAL_PRICE_RESPONSE = R.raw.ratesv3_tokonow_with_additional_price_response
    val RATES_V3_TOKONOW_WITH_NORMAL_PRICE_RESPONSE = R.raw.ratesv3_tokonow_with_normal_price_response

    const val VALIDATE_USE_KEY = "validate_use_promo_revamp"
    val VALIDATE_USE_DEFAULT_RESPONSE = R.raw.validate_use_analytics_default_response
    val VALIDATE_USE_TOKONOW_RESPONSE = R.raw.validate_use_tokonow_default_response
    
    const val CHECKOUT_KEY = "checkout"
    val CHECKOUT_DEFAULT_RESPONSE = R.raw.checkout_analytics_default_response
}