package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 21/05/20
 */

data class GetCarouselDataResponse(
        @Expose
        @SerializedName("fetchCarouselWidgetData")
        val carouselData: GetCarouselDataModel
)

data class GetCarouselDataModel(
        @Expose
        @SerializedName("data")
        val data: List<CarouselDataModel>
)

data class CarouselDataModel(
        @Expose
        @SerializedName("dataKey")
        val dataKey: String,
        @Expose
        @SerializedName("items")
        val items: List<CarouselItemModel>,
        @Expose
        @SerializedName("errorMsg")
        val errorMsg: String,
        @Expose
        @SerializedName("showWidget")
        val showWidget: Boolean
)

data class CarouselItemModel(
        @Expose
        @SerializedName("ID")
        val id: String,
        @Expose
        @SerializedName("URL")
        val url: String,
        @Expose
        @SerializedName("CreativeName")
        val creativeName: String,
        @Expose
        @SerializedName("AppLink")
        val appLink: String,
        @Expose
        @SerializedName("FeaturedMediaURL")
        val mediaUrl: String
)