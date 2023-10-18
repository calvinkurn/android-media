package com.tokopedia.tokofood.common.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood

data class CartListTokofoodParam(
    @SerializedName("source")
    val source: String = String.EMPTY,
    @SerializedName("business_data")
    val businessData: List<CartListTokofoodParamBusinessData> = listOf()
)

data class CartListTokofoodParamBusinessData(
    @SerializedName("business_id")
    val businessId: String = String.EMPTY,
    @SerializedName("custom_request")
    val customRequest: CartAdditionalAttributesTokoFood = CartAdditionalAttributesTokoFood()
)
