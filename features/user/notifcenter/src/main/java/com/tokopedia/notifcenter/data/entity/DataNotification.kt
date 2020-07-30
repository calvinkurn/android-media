package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataNotification(
        @SerializedName("app_link")
        @Expose
        val appLink: String = "",
        @SerializedName("checkout_url")
        @Expose
        val checkoutUrl: String = "",
        @SerializedName("desktop_link")
        @Expose
        val desktopLink: String = "",
        @SerializedName("info_thumbnail_url")
        @Expose
        val infoThumbnailUrl: String = "",
        @SerializedName("mobile_link")
        @Expose
        val mobileLink: String = ""
)