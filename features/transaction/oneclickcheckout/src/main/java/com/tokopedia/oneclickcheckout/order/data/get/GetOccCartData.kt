package com.tokopedia.oneclickcheckout.order.data.get

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSAFResponse
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker

data class GetOccCartData(
        @SerializedName("messages")
        val messages: CartMessages = CartMessages(),
        @SerializedName("tickers")
        val tickers: List<Ticker> = emptyList(),
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
        val promo: PromoSAFResponse? = null,
        @SerializedName("customer_data")
        val customerData: CustomerData = CustomerData(),
        @SerializedName("payment_additional_data")
        val paymentAdditionalData: PaymentAdditionalData = PaymentAdditionalData(),
        @SerializedName("error_ticker")
        val errorTicker: String = "",
        @SerializedName("prompt")
        val prompt: OccPromptResponse = OccPromptResponse(),
        @SerializedName("occ_revamp")
        val revamp: OccRevampResponse = OccRevampResponse(),
        @SerializedName("error_code")
        val errorCode: String = "",
        @SerializedName("pop_up_message")
        val popUpMessage: String = "",
        @SerializedName("occ_remove_profile")
        val removeProfile: OccRemoveProfileResponse = OccRemoveProfileResponse()
)

data class CartMessages(
        @SerializedName("ErrorProductAvailableStock")
        val messageErrorAvailableStock: String = "",
        @SerializedName("ErrorProductMaxQuantity")
        val messageErrorMaxQuantity: String = "",
        @SerializedName("ErrorProductMinQuantity")
        val messageErrorMinQuantity: String = ""
)

data class CustomerData(
        @SuppressLint("Invalid Data Type")
        @SerializedName("id")
        val id: Long = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("email")
        val email: String = "",
        @SerializedName("msisdn")
        val msisdn: String = ""
)

data class PaymentAdditionalData(
        @SerializedName("merchant_code")
        val merchantCode: String = "",
        @SerializedName("profile_code")
        val profileCode: String = "",
        @SerializedName("signature")
        val signature: String = "",
        @SerializedName("change_cc_link")
        val changeCcLink: String = "",
        @SerializedName("callback_url")
        val callbackUrl: String = ""
)

data class OccRevampResponse(
        @SerializedName("enable")
        val isEnable: Boolean = false,
        @SerializedName("total_profile")
        val totalProfile: Int = 0,
        @SerializedName("change_template_text")
        val changeTemplateText: String = ""
)