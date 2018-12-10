package com.tokopedia.chatbot.domain

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 16/05/18.
 */
class InvoiceLinkAttributePojo {

    @SerializedName("code")
    var code: String? = null
    @SerializedName("create_time")
    var createTime: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("href_url")
    var hrefUrl: String? = null
    @SerializedName("id")
    var id: Long = 0
    @SerializedName("image_url")
    var imageUrl: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("status_id")
    var statusId: Int = 0
    @SerializedName("title")
    var title: String? = null
    @SerializedName("total_amount")
    var totalAmount: String? = null
}
