package com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Flashsale (

    @SerializedName("status")
    @Expose
    val status: String = "",
    @SerializedName("applink")
    @Expose
    val appLink: String = "",
    @SerializedName("campaign_id")
    @Expose
    val campaignId: String = "",
    @SerializedName("campaign_name")
    @Expose
    val campaignName: String = "",
    @SerializedName("campaign_short_name")
    @Expose
    val campaignShortName: String = "",
    @SerializedName("start_date")
    @Expose
    val startDate: Long = 0,
    @SerializedName("end_date")
    @Expose
    val endDate: Long = 0,
    @SerializedName("products")
    @Expose
    val products: List<Product> = arrayListOf()
){
}
