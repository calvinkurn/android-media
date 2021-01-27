package com.tokopedia.shop.common.data.source.cloud.model.followshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Toaster(
        @SerializedName("toasterText")
        @Expose
        val toasterText: String?,
        @SerializedName("buttonType")
        @Expose
        val buttonType: String?,
        @SerializedName("buttonLabel")
        @Expose
        val buttonLabel: String?,
        @SerializedName("url")
        @Expose
        val url: String?,
        @SerializedName("appLink")
        @Expose
        val appLink: String?
)