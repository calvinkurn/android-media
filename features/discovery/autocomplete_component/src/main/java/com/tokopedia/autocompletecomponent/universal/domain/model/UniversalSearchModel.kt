package com.tokopedia.autocompletecomponent.universal.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class UniversalSearchModel(
    @SerializedName("universe_universal_search")
    @Expose
    val universalSearch: UniversalSearch = UniversalSearch(),
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

        @SuppressLint("Invalid Data Type")
        @SerializedName("price_int")
        @Expose
        val priceInt: Int = 0,

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

        @SerializedName("free_ongkir")
        @Expose
        val freeOngkir: FreeOngkir = FreeOngkir(),

        @SerializedName("label_groups")
        @Expose
        val labelGroup: List<LabelGroup> = listOf(),
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

    internal data class FreeOngkir(
        @SerializedName("img_url")
        @Expose
        val imgUrl: String = "",

        @SerializedName("is_active")
        @Expose
        val isActive: Boolean = false
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

    internal data class LabelGroup(
        @SerializedName("position")
        val position: String,

        @SerializedName("title")
        val title : String,

        @SerializedName("type")
        val type: String,

        @SerializedName("url")
        val url: String,
    )

    internal fun getItems(): List<UniversalSearchItem> {
        return universalSearch.universalSearchData.items
    }
}
