package com.tokopedia.tokochat.domain.response.orderprogress

import com.google.gson.annotations.SerializedName

data class TokoChatOrderProgressResponse(
    @SerializedName("tokochatOrderProgress")
    val tokochatOrderProgress: TokoChatOrderProgress = TokoChatOrderProgress()
) {
    data class TokoChatOrderProgress(
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
        @SerializedName("state")
        var state: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("statusId")
        val statusId: Long = 0,
        @SerializedName("uri")
        val uri: String = ""
    ) {
        data class Label(
            @SerializedName("title")
            val title: String = "",
            @SerializedName("value")
            val value: String = ""
        )
    }
}
