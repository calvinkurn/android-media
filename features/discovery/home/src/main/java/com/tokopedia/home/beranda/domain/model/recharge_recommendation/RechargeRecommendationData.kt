package com.tokopedia.home.beranda.domain.model.recharge_recommendation


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeRecommendationData(
        @Expose
        @SerializedName("contentID")
        val contentID: String = "",
        @Expose
        @SerializedName("mainText")
        val mainText: String = "",
        @Expose
        @SerializedName("subText")
        val subText: String = "",
        @Expose
        @SerializedName("applink")
        val applink: String = "",
        @Expose
        @SerializedName("link")
        val link: String = "",
        @Expose
        @SerializedName("iconURL")
        val iconURL: String = "",
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("backgroundColor")
        val backgroundColor: String = "",
        @Expose
        @SerializedName("buttonText")
        val buttonText: String = ""
)