package com.tokopedia.chatbot.domain.pojo.invoicelist.websocket

import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 16/05/18.
 */
class InvoiceSingleItemAttributes {

    @SerializedName("code")
    var code: String = ""
    @SerializedName("create_time")
    var createdTime: String = ""
    @SerializedName("description")
    var description: String = ""
    @SerializedName("url")
    var url: String = ""
    @SerializedName("id")
    var id: Long = 0
    @SerializedName("image_url")
    var imageUrl: String = ""
    @SerializedName("status")
    var status: String = ""
    @SerializedName("status_id")
    var statusId: Int = 0
    @SerializedName("title")
    var title: String = ""
    @SerializedName("total_amount")
    var amount: String = ""
}
