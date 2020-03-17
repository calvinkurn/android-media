package com.tokopedia.topchat.chatroom.domain.pojo


import com.google.gson.annotations.SerializedName

data class QuotationPojo(
    @SerializedName("identifier")
    val identifier: String = "",
    @SerializedName("price")
    val price: String = "",
    @SerializedName("thumbnail")
    val thumbnail: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = ""
)

data class QuotationAttributes(
        @SerializedName("quotation_profile")
        val quotation: QuotationPojo
)