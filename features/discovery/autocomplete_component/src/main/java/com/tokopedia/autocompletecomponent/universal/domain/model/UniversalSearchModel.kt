package com.tokopedia.autocompletecomponent.universal.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class UniversalSearchModel(
    @SerializedName("universe_universal_search")
    @Expose
    val universalSearch: UniversalSearch,
) {
    internal data class UniversalSearch(
        @SerializedName("header")
        @Expose
        val universalSearchHeader: UniversalSearchHeader = UniversalSearchHeader(),

        @SerializedName("data")
        @Expose
        val universalSearchData: UniversalSearchData = UniversalSearchData(),
    )

    internal data class UniversalSearchHeader(
        @SerializedName("status_code")
        @Expose
        val statusCode: Int = 0,

        @SerializedName("message")
        @Expose
        val message: String = "",

        @SerializedName("time_process")
        @Expose
        val timeProcess: Int = 0,
    )

    internal data class UniversalSearchData(
        @SerializedName("items")
        @Expose
        val items: List<UniversalSearchItem> = listOf(),
    )

    internal data class UniversalSearchItem(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("applink")
        @Expose
        val applink: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("template")
        @Expose
        val template: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("subtitle")
        @Expose
        val subtitle: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("component_id")
        @Expose
        val componentId: String = "",

        @SerializedName("tracking_option")
        @Expose
        val trackingOption: Int = 0,

        @SerializedName("campaign_code")
        @Expose
        val campaignCode: String = "",

        @SerializedName("product")
        @Expose
        val product: List<Product> = listOf(),

        @SerializedName("curated")
        @Expose
        val curated: List<Curated> = listOf(),
    )

    internal data class Product(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("applink")
        @Expose
        val applink: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("component_id")
        @Expose
        val componentId: String = "",

        @SerializedName("tracking_option")
        @Expose
        val trackingOption: Int = 0,

        @SerializedName("price")
        @Expose
        val price: String = "",

        @SerializedName("original_price")
        @Expose
        val originalPrice: String = "",

        @SerializedName("discount_percentage")
        @Expose
        val discountPercentage: String = "",

        @SerializedName("rating_average")
        @Expose
        val ratingAverage: String = "",

        @SerializedName("count_sold")
        @Expose
        val countSold: String = "",

        @SerializedName("shop")
        @Expose
        val shop: Shop = Shop(),

        @SerializedName("badge")
        @Expose
        val badge: List<Badge> = listOf(),
    )

    internal data class Shop(
        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("city")
        @Expose
        val city: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",
    )

    internal data class Badge(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("show")
        @Expose
        val show: Boolean = false,
    )

    internal data class Curated(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("applink")
        @Expose
        val applink: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("component_id")
        @Expose
        val componentId: String = "",

        @SerializedName("tracking_option")
        @Expose
        val trackingOption: Int = 0,

        @SerializedName("campaign_code")
        @Expose
        val campaignCode: String = "",
    )
}
