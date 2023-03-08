package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class AddOnWording(
    @SerializedName("packaging_and_greeting_card")
    val packagingAndGreetingCard: String = "",
    @SerializedName("only_greeting_card")
    val onlyGreetingCard: String = "",
    @SerializedName("invoice_not_sent_to_recipient")
    val invoiceNotSendToRecipient: String = ""
)
