package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSAFResponse

data class GetOccCartData(
        @SerializedName("messages")
        val messages: CartMessages = CartMessages(),
        @SerializedName("ticker_message")
        val tickerMessage: OccTickerMessage = OccTickerMessage(),
        @SerializedName("occ_main_onboarding")
        val occMainOnboarding: OccMainOnboarding = OccMainOnboarding(),
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
        @SerializedName("profile_recommendation_wording")
        val profileRecommendation: String = "",
        @SerializedName("profile")
        val profileResponse: ProfileResponse = ProfileResponse(),
        @SerializedName("promo")
        val promo: PromoSAFResponse? = null
)

data class CartMessages(
        @SerializedName("ErrorProductAvailableStock")
        val messageErrorAvailableStock: String = "",
        @SerializedName("ErrorProductMaxQuantity")
        val messageErrorMaxQuantity: String = "",
        @SerializedName("ErrorProductMinQuantity")
        val messageErrorMinQuantity: String = ""
)