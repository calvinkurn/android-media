package com.tokopedia.emoney.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeEmoneyInquiryResponse(
        @SerializedName("rechargeEmoneyInquiry")
        @Expose
        val rechargeEmoneyInquiry: RechargeEmoneyInquiry
)

class RechargeEmoneyInquiry(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("attributes")
        @Expose
        val attributesEmoneyInquiry: AttributesEmoneyInquiry? = null,
        @SerializedName("error")
        @Expose
        val error: RechargeEmoneyInquiryError? = null
)

class AttributesEmoneyInquiry(
        @SerializedName("button_text")
        @Expose
        val buttonText: String = "",
        @SerializedName("card_number")
        @Expose
        val cardNumber: String = "",
        @SerializedName("image_issuer")
        @Expose
        val imageIssuer: String = "",
        @SerializedName("last_balance")
        @Expose
        val lastBalance: Int = 0,
        @SerializedName("payload")
        @Expose
        val payload: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        var formattedCardNumber: String = ""
)

class RechargeEmoneyInquiryError(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0
)