package com.tokopedia.flashsale.management.data.campaignlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StatusInfo(
        @SerializedName("status_name")
        @Expose val name: String = "",

        @SerializedName("status_label")
        @Expose val label: String = "",

        @SerializedName("label_hover_text")
        @Expose val labelHover: String = "",

        @SerializedName("status_head")
        @Expose val head: String = "",

        @SerializedName("status_sub_text")
        @Expose val subText: String = ""
)