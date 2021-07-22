package com.tokopedia.oneclickcheckout.order.data.get

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSAFResponse
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker

class GetOccCartData(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("error_code")
        val errorCode: String = "",
        @SerializedName("pop_up_message")
        val popUpMessage: String = "",
        @SerializedName("max_char_note")
        val maxCharNote: Int = 0,
        @SerializedName("kero_token")
        val keroToken: String = "",
        @SerializedName("kero_unix_time")
        val keroUnixTime: String = "",
        @SerializedName("kero_discom_token")
        val keroDiscomToken: String = "",
        @SerializedName("error_ticker")
        val errorTicker: String = "",
        @SerializedName("tickers")
        val tickers: List<Ticker> = emptyList(),
        @SerializedName("occ_main_onboarding")
        val occMainOnboarding: OccMainOnboardingResponse = OccMainOnboardingResponse(),
        @SerializedName("group_shop_occ")
        val groupShop: List<GroupShopOccResponse> = emptyList(),
        @SerializedName("profile")
        val profileResponse: ProfileResponse = ProfileResponse(),
        @SerializedName("promo")
        val promo: PromoSAFResponse? = null,
        @SerializedName("customer_data")
        val customerData: CustomerData = CustomerData(),
        @SerializedName("payment_additional_data")
        val paymentAdditionalData: PaymentAdditionalData = PaymentAdditionalData(),
        @SerializedName("prompt")
        val prompt: OccPromptResponse = OccPromptResponse()
)

class CustomerData(
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

class PaymentAdditionalData(
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