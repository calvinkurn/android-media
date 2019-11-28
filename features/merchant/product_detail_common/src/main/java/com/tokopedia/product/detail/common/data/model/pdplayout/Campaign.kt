package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class Campaign(
    @SerializedName("appLinks")
    val appLinks: String = "",
    @SerializedName("campaignID")
    val campaignID: String = "",
    @SerializedName("campaignType")
    val campaignType: String = "",
    @SerializedName("campaignTypeName")
    val campaignTypeName: String = "",
    @SerializedName("discountedPrice")
    val discountedPrice: Int = 0,
    @SerializedName("endDate")
    val endDate: String = "",
    @SerializedName("endDateUnix")
    val endDateUnix: String = "",
    @SerializedName("hideGimmick")
    val hideGimmick: Boolean = false,
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("isAppsOnly")
    val isAppsOnly: Boolean = false,
    @SerializedName("originalPrice")
    val originalPrice: Int = 0,
    @SerializedName("originalStock")
    val originalStock: Int = 0,
    @SerializedName("percentageAmount")
    val percentageAmount: Int = 0,
    @SerializedName("startDate")
    val startDate: String = "",
    @SerializedName("stock")
    val stock: Int = 0
)