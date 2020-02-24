package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 2020-02-11
 */
 
data class CarouselDataResponse(
        @Expose
        @SerializedName("getCarouselWidgetData")
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
        val errorMsg: String
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