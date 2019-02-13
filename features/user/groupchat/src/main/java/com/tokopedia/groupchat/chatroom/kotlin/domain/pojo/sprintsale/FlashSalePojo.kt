package com.tokopedia.groupchat.chatroom.kotlin.domain.pojo.sprintsale

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.groupchat.chatroom.kotlin.domain.pojo.BaseGroupChatPojo

class FlashSalePojo : BaseGroupChatPojo() {

    @SerializedName("applink")
    @Expose
    val appLink: String? = null
    @SerializedName("campaign_id")
    @Expose
    val campaignId: String? = null
    @SerializedName("campaign_name")
    @Expose
    val campaignName: String? = null
    @SerializedName("campaign_short_name")
    @Expose
    val campaignShortName: String? = null
    @SerializedName("start_date")
    @Expose
    val startDate: Long = 0
    @SerializedName("end_date")
    @Expose
    val endDate: Long = 0
    @SerializedName("products")
    @Expose
    val products: List<Product>? = null
}
