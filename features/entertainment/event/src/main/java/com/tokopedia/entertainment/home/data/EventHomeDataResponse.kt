package com.tokopedia.entertainment.home.data


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class EventHomeDataResponse(
        @SerializedName("data")
        val `data`: Data = Data()
) {
    data class Data(
            @SerializedName("event_child_category")
            val eventChildCategory: EventChildCategory = EventChildCategory(),
            @SerializedName("event_home")
            val eventHome: EventHome = EventHome(),
            @SerializedName("event_location_search")
            val eventLocationSearch: EventLocationSearch = EventLocationSearch()
    ) {
        data class EventChildCategory(
                @SerializedName("categories")
                val categories: MutableList<Category> = mutableListOf()
        ) {
            data class Category(
                    @SerializedName("app_url")
                    val appUrl: String = "",
                    @SerializedName("id")
                    val id: String = "",
                    @SerializedName("media_url")
                    val mediaUrl: String = "",
                    @SerializedName("url")
                    val url: String = "",
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("priority")
                    val priority: String = "",
                    @SerializedName("title")
                    val title: String = ""
            )
        }

        data class EventHome(
                @SerializedName("layout")
                val layout: MutableList<Layout> = mutableListOf()
        ) {
            data class Layout(
                    @SerializedName("app_url")
                    val appUrl: String = "",
                    @SerializedName("category_url")
                    val categoryUrl: String = "",
                    @SerializedName("id")
                    val id: String = "",
                    @SerializedName("is_card")
                    val isCard: Int = 0,
                    @SerializedName("items")
                    val items: List<Item> = listOf(),
                    @SerializedName("title")
                    val title: String = ""
            ) {
                data class Item(
                        @SerializedName("is_free")
                        val isFree: Int = 0,
                        @SerializedName("app_url")
                        val appUrl: String = "",
                        @SerializedName("url")
                        val url: String = "",
                        @SerializedName("city_name")
                        val cityName: String = "",
                        @SerializedName("display_name")
                        val displayName: String = "",
                        @SerializedName("id")
                        val id: String = "0",
                        @SerializedName("rating")
                        val rating: Int = 0,
                        @SerializedName("image_app")
                        val imageApp: String = "",
                        @SerializedName("location")
                        val location: String = "",
                        @SuppressLint("Invalid Data Type")
                        @SerializedName("price")
                        val price: String = "",
                        @SerializedName("sales_price")
                        val salesPrice: String = "",
                        @SerializedName("schedule")
                        val schedule: String = "",
                        @SerializedName("title")
                        val title: String = "",
                        @SerializedName("is_liked")
                        var isLiked: Boolean,
                        @SerializedName("is_promo")
                        var isPromo: Int = 0,
                        @SerializedName("seo_url")
                        val seoUrl: String = "",
                        @SerializedName("min_start_date")
                        val minStartDate: String = ""
                )
            }
        }

        data class EventLocationSearch(
                @SerializedName("count")
                val count: String = "",
                @SerializedName("locations")
                val locations: List<Location> = listOf()
        ) {
            data class Location(
                    @SerializedName("city_id")
                    val cityId: String = "",
                    @SerializedName("coordinates")
                    val coordinates: String = "",
                    @SerializedName("id")
                    val id: String = "",
                    @SerializedName("image_app")
                    val imageApp: String = "",
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("priority")
                    val priority: String = "",
                    @SerializedName("address")
                    val address: String = "",
                    @SerializedName("location_type")
                    val locationType: LocationType = LocationType()
            )

            data class LocationType(
                    @SerializedName("display_name")
                    val displayName: String = "",
                    @SerializedName("id")
                    val id: String = "0",
                    @SerializedName("name")
                    val name: String = ""
            )
        }
    }
}