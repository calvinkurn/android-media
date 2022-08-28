package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName


data class FeedXCampaign(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("shortName")
    var shortName: String = "",
    @SerializedName("startTime")
    var startTime: String = "",
    @SerializedName("endTime")
    var endTime: String = "",
)
