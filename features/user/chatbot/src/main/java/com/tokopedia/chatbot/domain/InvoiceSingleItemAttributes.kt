package com.tokopedia.chatbot.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 16/05/18.
 */
class InvoiceSingleItemAttributes {

    @SerializedName("code")
    var code: String? = null
    @SerializedName("create_time")
    var createdTime: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("url")
    var url: String? = null
    @SerializedName("id")
    var id: Long? = null
    @SerializedName("image_url")
    var imageUrl: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("status_id")
    var statusId: Int = 0
    @SerializedName("title")
    var title: String? = null
    @SerializedName("total_amount")
    var amount: String? = null
}
