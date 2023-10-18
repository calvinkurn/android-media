package com.tokopedia.tokofood.feature.purchase.promopage.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddress

data class PromoTokoFoodParam(
    @SerializedName("source")
    val source: String = "",
    @SerializedName("business_data")
    val businessData: List<PromoTokofoodParamBusinessData> = listOf()
)

data class PromoTokofoodParamBusinessData(
    @SerializedName("business_id")
    val businessId: String = String.EMPTY,
    @SerializedName("custom_request")
    val customRequest: PromoTokofoodParamCustomRequest = PromoTokofoodParamCustomRequest(),
    @SerializedName("carts")
    val carts: List<PromoTokofoodParamCart> = listOf()
)

data class PromoTokofoodParamCustomRequest(
    @SerializedName("chosen_address")
    val chosenAddress: TokoFoodChosenAddress = TokoFoodChosenAddress(),
    @SerializedName("merchant_id")
    val merchantId: String = String.EMPTY
)

data class PromoTokofoodParamCart(
    @SerializedName("cart_id")
    val cartId: String = String.EMPTY
)
