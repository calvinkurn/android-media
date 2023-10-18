package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.SerializedName

data class ProductMediaRecomBasicInfo(
    @SerializedName("lightIcon")
    val lightIcon: String = "",
    @SerializedName("darkIcon")
    val darkIcon: String = "",
    @SerializedName("iconText")
    val iconText: String = "",
    @SerializedName("bottomsheetTitle")
    val bottomsheetTitle: String = "",
    @SerializedName("recommendation")
    val recommendation: String = ""
)
