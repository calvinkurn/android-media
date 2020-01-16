package com.tokopedia.topads.detail_sheet.data


import com.google.gson.annotations.SerializedName

data class ActionRequest(
    @SerializedName("data")
    val `data`: Bulk
)