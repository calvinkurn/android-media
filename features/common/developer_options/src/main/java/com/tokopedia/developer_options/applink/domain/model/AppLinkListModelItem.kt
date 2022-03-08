package com.tokopedia.developer_options.applink.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class AppLinkListModelItem(
    @SerializedName("deeplink")
    @Expose
    val deepLink: String = "",
    @SerializedName("deeplink_variable")
    @Expose
    val deepLinkVariable: String? = "",
    @SerializedName("destination_activity")
    @Expose
    val destinationActivity: String = ""
)