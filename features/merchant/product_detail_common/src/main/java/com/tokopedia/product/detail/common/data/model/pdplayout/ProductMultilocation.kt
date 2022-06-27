package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductMultilocation(
        @SerializedName("isReroute")
        @Expose
        val isReroute: Boolean = false,
        @SerializedName("cityName")
        @Expose
        val cityName: String = "",
        @SerializedName("eduLink")
        @Expose
        val eduLink: ProductEduLink = ProductEduLink()
)

data class ProductEduLink(
        @SerializedName("appLink")
        @Expose
        val applink: String = ""
)
