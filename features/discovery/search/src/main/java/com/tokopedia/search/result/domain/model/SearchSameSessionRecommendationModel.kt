package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchSameSessionRecommendationModel(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("component_id")
    @Expose
    val componentId: String = "",

    @SerializedName("tracking_option")
    @Expose
    val trackingOption: String = "0",

    @SerializedName("products")
    @Expose
    val products: List<SearchProductModel.InspirationCarouselProduct> = listOf(),

    @SerializedName("feedback")
    @Expose
    val feedback: Feedback = Feedback(),
) {
    data class Feedback(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("component_id")
        @Expose
        val componentId: String = "",

        @SerializedName("tracking_option")
        @Expose
        val trackingOption: String = "0",

        @SerializedName("data")
        @Expose
        val data: List<Data> = listOf(),
    ) {
        data class Data(
            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("image_url")
            val imageUrl: String = "",

            @SerializedName("component_id")
            @Expose
            val componentId: String = "",

            @SerializedName("title_on_click")
            @Expose
            val titleOnClick: String = "",

            @SerializedName("message_on_click")
            @Expose
            val messageOnClick: String = "",
        )
    }
}
