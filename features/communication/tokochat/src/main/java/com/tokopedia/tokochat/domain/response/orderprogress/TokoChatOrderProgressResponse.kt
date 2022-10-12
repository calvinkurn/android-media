package com.tokopedia.tokochat.domain.response.orderprogress


import com.google.gson.annotations.SerializedName

data class TokoChatOrderProgressResponse(
    @SerializedName("data")
    val `data`: Data = Data()
) {
    data class Data(
        @SerializedName("chatOrderProgress")
        val chatOrderProgress: ChatOrderProgress = ChatOrderProgress()
    ) {
        data class ChatOrderProgress(
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
            val state: String = "",
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
}
