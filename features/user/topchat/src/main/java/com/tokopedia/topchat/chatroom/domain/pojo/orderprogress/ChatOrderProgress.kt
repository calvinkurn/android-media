package com.tokopedia.topchat.chatroom.domain.pojo.orderprogress


import com.google.gson.annotations.SerializedName

data class ChatOrderProgress(
        @SerializedName("button")
        val button: Button = Button(),
        @SerializedName("enable")
        val enable: Boolean = false,
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("invoiceId")
        val invoiceId: String = "",
        @SerializedName("label")
        val label: Label = Label(),
        @SerializedName("name")
        val name: String = "",
        @SerializedName("orderId")
        val orderId: String = "",
        @SerializedName("shipment")
        val shipment: Shipment = Shipment(),
        @SerializedName("state")
        val state: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("statusId")
        val statusId: Int = 0,
        @SerializedName("uri")
        val uri: String = ""
)