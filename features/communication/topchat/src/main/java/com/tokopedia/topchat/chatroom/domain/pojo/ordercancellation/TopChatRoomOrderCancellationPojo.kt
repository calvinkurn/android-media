package com.tokopedia.topchat.chatroom.domain.pojo.ordercancellation

import com.google.gson.annotations.SerializedName

data class TopChatRoomOrderCancellationPojo(
    @SerializedName("order_id")
    val orderId: String = "0",

    @SerializedName("order_status")
    val orderStatus: Int = 0,

    @SerializedName("invoice_ref_num")
    val invoiceId: String = "",

    @SerializedName("button_attributes")
    val button: TopChatRoomOrderCancellationButtonPojo = TopChatRoomOrderCancellationButtonPojo()
)
