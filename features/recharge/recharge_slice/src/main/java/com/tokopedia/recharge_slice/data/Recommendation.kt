package com.tokopedia.recharge_slice.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Recommendation(
    @SerializedName("productId")
    @Expose
    val productId: Int = 0,
    @SerializedName("iconUrl")
    @Expose
    val iconUrl: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("clientNumber")
    @Expose
    val clientNumber: String = "",
    @SerializedName("appLink")
    @Expose
    val appLink: String = "",
    @SerializedName("webLink")
    @Expose
    val webLink: String = "",
    @SerializedName("position")
    @Expose
    val position: Int = 0,
    @SerializedName("categoryName")
    @Expose
    val categoryName: String = "Loading...",
    @SerializedName("categoryId")
    @Expose
    val categoryId: Int = 0,
    @SerializedName("productName")
    @Expose
    val productName: String = "",
    @SerializedName("type")
    @Expose
    val type: String = "",
    @SerializedName("productPrice")
    @Expose
    val productPrice: Int = 0,
    @SerializedName("operatorName")
    @Expose
    val operatorName: String = ""
)