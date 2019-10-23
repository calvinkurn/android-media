package com.tokopedia.topads.detail_sheet.data


import com.google.gson.annotations.SerializedName

data class BulkActionRequest(
    @SerializedName("data")
    val `data`: DataBulk
)