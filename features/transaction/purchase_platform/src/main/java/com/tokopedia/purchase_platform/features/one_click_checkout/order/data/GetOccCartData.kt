package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.PromoSAFResponse

data class GetOccCartData(
        @SerializedName("kero_token")
        val keroToken: String = "",
        @SerializedName("kero_discom_token")
        val keroDiscomToken: String = "",
        @SerializedName("kero_unix_time")
        val keroUnixTime: String = "",
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("cart_list")
        val cartList: List<CartDataResponse> = emptyList(),
        @SerializedName("profile_index_wording")
        val profileIndex: String = "",
        @SerializedName("profile")
        val profileResponse: ProfileResponse = ProfileResponse(),
        @SerializedName("promo")
        val promo: PromoSAFResponse? = null
)