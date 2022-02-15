package com.tokopedia.purchase_platform.common.feature.gifting.data.response

import com.google.gson.annotations.SerializedName

data class AddOnWording(
        @SerializedName("packaging_and_greeting_card")
        val packagingAndGreetingCard: String = "",
        @SerializedName("only_greeting_card")
        val onlyGreetingCard: String = "",
        @SerializedName("invoice_not_send_to_recipient")
        val invoiceNotSendToRecipient: String = ""
)