package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScoreResult(
        @SerializedName("Data")
        @Expose
        val data: ShopScoreDetailItemServiceModel = ShopScoreDetailItemServiceModel(),
        @SerializedName("BadgeScore")
        @Expose
        val badgeScore: Int = 0
)

data class ShopScoreDetailItemServiceModel(
        @SerializedName("Title")
        @Expose
        val title: String? = "",
        @SerializedName("Value")
        @Expose
        val value: Long = 0,
        @SerializedName("Description")
        @Expose
        val description: String? = "",
        @SerializedName("Color")
        @Expose
        val color: String? = ""
)