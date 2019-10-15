package com.tokopedia.createpost.data.pojo.productsuggestion.affiliate


import com.google.gson.annotations.SerializedName

data class AffiliateProductItem(
    @SerializedName("ad_id")
    val adId: Int = 0,
    @SerializedName("app_link")
    val appLink: String = "",
    @SerializedName("category_id")
    val categoryId: Int = 0,
    @SerializedName("commission_percent")
    val commissionPercent: Int = 0,
    @SerializedName("commission_percent_display")
    val commissionPercentDisplay: String = "",
    @SerializedName("commission_value")
    val commissionValue: Int = 0,
    @SerializedName("commission_value_display")
    val commissionValueDisplay: String = "",
    @SerializedName("fav_icon")
    val favIcon: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("lite_link")
    val liteLink: String = "",
    @SerializedName("product_id")
    val productId: Int = 0,
    @SerializedName("ratio")
    val ratio: Int = 0,
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("web_link")
    val webLink: String = ""
)