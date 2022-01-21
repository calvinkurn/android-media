package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class DataNotification(
    @SerializedName("app_link")
    val appLink: String = "",
    @SerializedName("checkout_url")
    val checkoutUrl: String = "",
    @SerializedName("desktop_link")
    val desktopLink: String = "",
    @SerializedName("info_thumbnail_url")
    val infoThumbnailUrl: String = "",
    @SerializedName("mobile_link")
    val mobileLink: String = "",
    @SerializedName("product_name")
    val productName: String = "",
    @SerializedName("product_url")
    val productUrl: String = ""
)