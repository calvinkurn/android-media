package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class Content(
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("subtitle")
        var subtitle: String = "",
        @SerializedName("applink")
        val applink: String = ""
)