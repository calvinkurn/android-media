package com.tokopedia.common_electronic_money.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmoneyInquiryResponse(
        @SerializedName("rechargeEmoneyInquiry")
        @Expose
        val emoneyInquiry: EmoneyInquiry
)

data class EmoneyInquiry(
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
        val error: EmoneyInquiryError? = null,
        val isCheckSaldoTapcash : Boolean = false,
        val isBCAGenOne: Boolean = false,
        val messageBCAGen1: String = "",
        val messageTopUp2: String = "",
        var isErrorTopUp2: Boolean = false
)

data class AttributesEmoneyInquiry(
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
        var formattedCardNumber: String = "",
        var issuer_id: Int = 0,
        var operatorId: String = "",
        var pendingBalance: Int = 0,
        var extraPendingBalance: Boolean = false,
        var showAdditionalBalance: Boolean = false
)

data class EmoneyInquiryError(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        var needAction: Boolean = false
)
