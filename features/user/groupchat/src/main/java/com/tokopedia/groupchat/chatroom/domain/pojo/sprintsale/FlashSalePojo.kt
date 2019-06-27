package com.tokopedia.groupchat.chatroom.domain.pojo.sprintsale

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.groupchat.chatroom.domain.pojo.BaseGroupChatPojo

class FlashSalePojo : BaseGroupChatPojo() {

    @SerializedName("applink")
    @Expose
    val appLink: String= ""
    @SerializedName("campaign_id")
    @Expose
    val campaignId: String= ""
    @SerializedName("campaign_name")
    @Expose
    val campaignName: String= ""
    @SerializedName("campaign_short_name")
    @Expose
    val campaignShortName: String= ""
    @SerializedName("start_date")
    @Expose
    val startDate: Long = 0
    @SerializedName("end_date")
    @Expose
    val endDate: Long = 0
    @SerializedName("products")
    @Expose
    val products: List<Product> = arrayListOf()
}
