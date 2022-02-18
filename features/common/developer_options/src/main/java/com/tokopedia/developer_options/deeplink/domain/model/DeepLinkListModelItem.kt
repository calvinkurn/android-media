package com.tokopedia.developer_options.deeplink.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DeepLinkListModelItem(
    @SerializedName("depplink")
    @Expose
    val depplink: String = "",
    @SerializedName("depplink_variable")
    @Expose
    val depplinkVariable: String = "",
    @SerializedName("destination_activity")
    @Expose
    val destinationActivity: String = ""
)