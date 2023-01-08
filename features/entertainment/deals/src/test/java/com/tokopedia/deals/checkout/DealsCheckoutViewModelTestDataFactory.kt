package com.tokopedia.deals.checkout

import com.google.gson.Gson
import com.tokopedia.common_entertainment.data.DealsCheckoutInstantResponse
import com.tokopedia.common_entertainment.data.DealsCheckoutResponse
import com.tokopedia.common_entertainment.data.DealsGeneral
import com.tokopedia.common_entertainment.data.DealsInstant
import com.tokopedia.deals.DealsJsonMapper

fun createParamGeneral(): DealsGeneral {
    return Gson().fromJson(
        DealsJsonMapper.getJson("deals_checkout_general_request.json"),
        DealsGeneral::class.java
    )
}

fun createParamGeneralNoPromo(): DealsGeneral {
    return Gson().fromJson(
        DealsJsonMapper.getJson("deals_checkout_general_no_promo_request.json"),
        DealsGeneral::class.java
    )
}

fun createParamGeneralInstant(): DealsInstant {
    return Gson().fromJson(
        DealsJsonMapper.getJson("deals_checkout_instant_request.json"),
        DealsInstant::class.java
    )
}

fun createParamGeneralInstantNoPromo(): DealsInstant {
    return Gson().fromJson(
        DealsJsonMapper.getJson("deals_checkout_instant_no_promo_request.json"),
        DealsInstant::class.java
    )
}

fun createGeneralResponse(): DealsCheckoutResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("deals_checkout_general_response.json"),
        DealsCheckoutResponse::class.java
    )
}

fun createGeneralInstantResponse(): DealsCheckoutInstantResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("deals_checkout_instant_response.json"),
        DealsCheckoutInstantResponse::class.java
    )
}
