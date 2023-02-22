package com.tokopedia.tokofood.common.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddress

data class ATCTokofoodParam(
    @SerializedName("source")
    val source: String = String.EMPTY,
    @SerializedName("business_data")
    val businessData: List<ATCTokofoodParamBusinessData> = listOf()
)

data class ATCTokofoodParamBusinessData(
    @SerializedName("business_id")
    val businessId: String = String.EMPTY,
    @SerializedName("custom_request")
    val customRequest: ATCTokofoodParamCustomRequest = ATCTokofoodParamCustomRequest(),
    @SerializedName("product_list")
    val productList: List<ATCTokofoodParamProduct> = listOf()
)

data class ATCTokofoodParamCustomRequest(
    @SerializedName("chosen_address")
    val chosenAddress: TokoFoodChosenAddress = TokoFoodChosenAddress()
)

data class ATCTokofoodParamProduct(
    @SerializedName("cart_id")
    val cartId: String = String.EMPTY,
    @SerializedName("product_id")
    val productId: String = String.EMPTY,
    @SerializedName("quantity")
    val quantity: Int = Int.ONE,
    @SerializedName("metadata")
    val metadata: ATCTokofoodParamMetadata = ATCTokofoodParamMetadata(),
    @SerializedName("shop_id")
    val shopId: String = String.EMPTY
)

data class ATCTokofoodParamMetadata(
    @SerializedName("notes")
    val notes: String = String.EMPTY,
    @SerializedName("variants")
    val variants: List<ATCTokofoodParamVariant> = listOf()
)

data class ATCTokofoodParamVariant(
    @SerializedName("option_id")
    val optionId: String = String.EMPTY,
    @SerializedName("variant_id")
    val variantId: String = String.EMPTY
)
