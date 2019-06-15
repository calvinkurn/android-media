package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScoreResult(
        @SerializedName("links")
        @Expose
        val links: Links? = Links(),
        @SerializedName("data")
        @Expose
        val data: ShopScoreDetailDataServiceModel? = ShopScoreDetailDataServiceModel())

data class Links(
        @SerializedName("self")
        @Expose
        val self: String? = ""

)

data class ShopScoreDetailDataServiceModel(
        @SerializedName("Data")
        @Expose
        val data: List<ShopScoreDetailItemServiceModel>? = listOf(),
        @SerializedName("BadgeScore")
        @Expose
        val badgeScore: Int? = 0,
        @SerializedName("SumData")
        @Expose
        val sumData: SumData? = SumData()

)

data class ShopScoreDetailItemServiceModel(
        @SerializedName("Title")
        @Expose
        val title: String? = "",
        @SerializedName("Value")
        @Expose
        val value: Int? = 0,
        @SerializedName("Description")
        @Expose
        val description: String? = "",
        @SerializedName("Color")
        @Expose
        val color: String? = ""
)

data class SumData(
        @SerializedName("Color")
        @Expose
        private var color: String? = "",
        @SerializedName("Text")
        @Expose
        private var text: String? = "",
        @SerializedName("Value")
        @Expose
        private var value: Int? = 0
)