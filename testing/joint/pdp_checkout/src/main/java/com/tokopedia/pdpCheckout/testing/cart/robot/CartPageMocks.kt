package com.tokopedia.pdpCheckout.testing.cart.robot

import com.tokopedia.pdpCheckout.testing.R

object CartPageMocks {

    const val GET_CART_LIST_KEY = "cart_revamp_v4"

    val GET_CART_LIST_MOCK_DEFAULT_RESPONSE = R.raw.cart_analytics_default_response
    val GET_CART_LIST_MOCK_PROMO_RESPONSE = R.raw.cart_analytics_promo_response
    val GET_CART_LIST_MOCK_BOE_RESPONSE = R.raw.cart_bebas_ongkir_extra_response
    val GET_CART_LIST_MOCK_HAPPY_FLOW_RESPONSE = R.raw.cart_happy_flow_response

    const val UPDATE_CART_KEY = "update_cart_v2"
    val UPDATE_CART_MOCK_DEFAULT_RESPONSE = R.raw.update_cart_response

    const val VALIDATE_USE_KEY = "validate_use_promo_revamp"
    val VALIDATE_USE_MOCK_DEFAULT_RESPONSE = R.raw.validate_use_analytics_default_response
}
