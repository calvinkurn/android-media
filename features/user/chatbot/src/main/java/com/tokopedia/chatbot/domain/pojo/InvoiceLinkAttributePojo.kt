package com.tokopedia.chatbot.domain.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 16/05/18.
 */
class InvoiceLinkAttributePojo {

    @SerializedName("code")
    var code: String = ""
    @SerializedName("create_time")
    var createTime: String = ""
    @SerializedName("description")
    var description: String = ""
    @SerializedName("href_url")
    var hrefUrl: String = ""
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
    var totalAmount: String = ""
}
