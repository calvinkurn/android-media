package com.tokopedia.entertainment.home.data

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 06,February,2020
 */
data class ResponseModel(
        @SerializedName("data")
        val data: Data = Data(),
        @SerializedName("errors")
        val error: List<ErrorResponse> = listOf(ErrorResponse())
){
    data class Data(
        @SerializedName("event_home")
        val eventHome: EventHome = EventHome()
    ) {
        data class EventHome(
                @SerializedName("layout")
            val layout: MutableList<Layout> = mutableListOf(),
                @SerializedName("meta_description")
            val metaDescription: String = "",
                @SerializedName("meta_title")
            val metaTitle: String = ""
        ) {
            data class Layout(
                @SerializedName("app_url")
                val appUrl: String = "",
                @SerializedName("category_url")
                val categoryUrl: String = "",
                @SerializedName("count")
                val count: String = "",
                @SerializedName("id")
                val id: String = "",
                @SerializedName("inactive_media_url")
                val inactiveMediaUrl: String = "",
                @SerializedName("is_card")
                val isCard: Int = 0,
                @SerializedName("items")
                val items: List<Item> = listOf(),
                @SerializedName("media_url")
                val mediaUrl: String = "",
                @SerializedName("priority")
                val priority: String = "",
                @SerializedName("seo_url")
                val seoUrl: String = "",
                @SerializedName("title")
                val title: String = "",
                @SerializedName("url")
                val url: String = "",
                @SerializedName("web_url")
                val webUrl: String = ""
            ) {
                data class Item(
                    @SerializedName("action_text")
                    val actionText: String = "",
                    @SerializedName("app_url")
                    val appUrl: String = "",
                    @SerializedName("autocode")
                    val autocode: String = "",
                    @SerializedName("brand")
                    val brand: Brand = Brand(),
                    @SerializedName("brand_id")
                    val brandId: String = "",
                    @SerializedName("buy_enabled")
                    val buyEnabled: Boolean = false,
                    @SerializedName("catalog")
                    val catalog: Catalog = Catalog(),
                    @SerializedName("category")
                    val category: List<Category> = listOf(),
                    @SerializedName("category_id")
                    val categoryId: String = "",
                    @SerializedName("censor")
                    val censor: String = "",
                    @SerializedName("child_category_ids")
                    val childCategoryIds: String = "",
                    @SerializedName("city_ids")
                    val cityIds: String = "",
                    @SerializedName("city_name")
                    val cityName: String = "",
                    @SerializedName("code")
                    val code: String = "",
                    @SerializedName("convenience_fee")
                    val convenienceFee: String = "",
                    @SerializedName("created_at")
                    val createdAt: String = "",
                    @SerializedName("custom_labels")
                    val customLabels: String = "",
                    @SerializedName("custom_text_1")
                    val customText1: String = "",
                    @SerializedName("custom_text_2")
                    val customText2: String = "",
                    @SerializedName("custom_text_4")
                    val customText4: String = "",
                    @SerializedName("custom_text_5")
                    val customText5: String = "",
                    @SerializedName("date_range")
                    val dateRange: Boolean = false,
                    @SerializedName("display_name")
                    val displayName: String = "",
                    @SerializedName("display_tags")
                    val displayTags: String = "",
                    @SerializedName("duration")
                    val duration: String = "",
                    @SerializedName("form")
                    val form: String = "",
                    @SerializedName("forms")
                    val forms: List<Any> = listOf(),
                    @SerializedName("genre")
                    val genre: String = "",
                    @SerializedName("has_popup")
                    val hasPopup: Boolean = false,
                    @SerializedName("has_seat_layout")
                    val hasSeatLayout: String = "",
                    @SerializedName("id")
                    val id: String = "",
                    @SerializedName("image_app")
                    val imageApp: String = "",
                    @SerializedName("image_web")
                    val imageWeb: String = "",
                    @SerializedName("is_featured")
                    val isFeatured: Int = 0,
                    @SerializedName("is_food_available")
                    val isFoodAvailable: Int = 0,
                    @SerializedName("is_liked")
                    val isLiked: Boolean = false,
                    @SerializedName("is_promo")
                    val isPromo: Int = 0,
                    @SerializedName("is_searchable")
                    val isSearchable: Int = 0,
                    @SerializedName("is_top")
                    val isTop: Int = 0,
                    @SerializedName("likes")
                    val likes: Int = 0,
                    @SerializedName("location")
                    val location: String = "",
                    @SerializedName("long_rich_desc")
                    val longRichDesc: String = "",
                    @SerializedName("max_end_date")
                    val maxEndDate: String = "",
                    @SerializedName("max_end_time")
                    val maxEndTime: String = "",
                    @SerializedName("media")
                    val media: List<Any> = listOf(),
                    @SerializedName("message")
                    val message: String = "",
                    @SerializedName("message_error")
                    val messageError: String = "",
                    @SerializedName("meta_description")
                    val metaDescription: String = "",
                    @SerializedName("meta_keywords")
                    val metaKeywords: String = "",
                    @SerializedName("meta_title")
                    val metaTitle: String = "",
                    @SerializedName("min_start_date")
                    val minStartDate: String = "",
                    @SerializedName("min_start_time")
                    val minStartTime: String = "",
                    @SerializedName("mrp")
                    val mrp: String = "",
                    @SerializedName("no_promo")
                    val noPromo: Boolean = false,
                    @SerializedName("offer_text")
                    val offerText: String = "",
                    @SerializedName("outlets")
                    val outlets: List<Any> = listOf(),
                    @SerializedName("parent_id")
                    val parentId: String = "",
                    @SerializedName("price")
                    val price: String = "",
                    @SerializedName("priority")
                    val priority: String = "",
                    @SerializedName("promotion_text")
                    val promotionText: String = "",
                    @SerializedName("provider_id")
                    val providerId: String = "",
                    @SerializedName("provider_product_code")
                    val providerProductCode: String = "",
                    @SerializedName("provider_product_id")
                    val providerProductId: String = "",
                    @SerializedName("provider_product_name")
                    val providerProductName: String = "",
                    @SerializedName("quantity")
                    val quantity: String = "",
                    @SerializedName("rating")
                    val rating: String = "",
                    @SerializedName("recommendation_url")
                    val recommendationUrl: String = "",
                    @SerializedName("redirect")
                    val redirect: Int = 0,
                    @SerializedName("remaining_sale_time")
                    val remainingSaleTime: String = "",
                    @SerializedName("sale_end_date")
                    val saleEndDate: String = "",
                    @SerializedName("sale_end_time")
                    val saleEndTime: String = "",
                    @SerializedName("sale_start_date")
                    val saleStartDate: String = "",
                    @SerializedName("sale_start_time")
                    val saleStartTime: String = "",
                    @SerializedName("sales_price")
                    val salesPrice: String = "",
                    @SerializedName("salient_features")
                    val salientFeatures: String = "",
                    @SerializedName("saving")
                    val saving: String = "",
                    @SerializedName("saving_percentage")
                    val savingPercentage: String = "",
                    @SerializedName("schedule")
                    val schedule: String = "",
                    @SerializedName("schedules")
                    val schedules: List<Any> = listOf(),
                    @SerializedName("search_tags")
                    val searchTags: String = "",
                    @SerializedName("seat_chart_type")
                    val seatChartType: String = "",
                    @SerializedName("seatmap_image")
                    val seatmapImage: String = "",
                    @SerializedName("sell_rate")
                    val sellRate: String = "",
                    @SerializedName("seo_url")
                    val seoUrl: String = "",
                    @SerializedName("short_desc")
                    val shortDesc: String = "",
                    @SerializedName("sold_quantity")
                    val soldQuantity: String = "",
                    @SerializedName("status")
                    val status: Int = 0,
                    @SerializedName("thumbnail_app")
                    val thumbnailApp: String = "",
                    @SerializedName("thumbnail_web")
                    val thumbnailWeb: String = "",
                    @SerializedName("thumbs_down")
                    val thumbsDown: String = "",
                    @SerializedName("thumbs_up")
                    val thumbsUp: String = "",
                    @SerializedName("title")
                    val title: String = "",
                    @SerializedName("tnc")
                    val tnc: String = "",
                    @SerializedName("updated_at")
                    val updatedAt: String = "",
                    @SerializedName("url")
                    val url: String = "",
                    @SerializedName("use_pdf")
                    val usePdf: Int = 0,
                    @SerializedName("web_url")
                    val webUrl: String = ""
                ) {
                    data class Brand(
                        @SerializedName("city_name")
                        val cityName: String = "",
                        @SerializedName("description")
                        val description: String = "",
                        @SerializedName("featured_image")
                        val featuredImage: String = "",
                        @SerializedName("featured_thumbnail_image")
                        val featuredThumbnailImage: String = "",
                        @SerializedName("id")
                        val id: String = "",
                        @SerializedName("seo_url")
                        val seoUrl: String = "",
                        @SerializedName("title")
                        val title: String = "",
                        @SerializedName("url")
                        val url: String = ""
                    )

                    data class Catalog(
                        @SerializedName("digital_category_id")
                        val digitalCategoryId: String = "",
                        @SerializedName("digital_product_code")
                        val digitalProductCode: String = "",
                        @SerializedName("digital_product_id")
                        val digitalProductId: String = ""
                    )

                    data class Category(
                        @SerializedName("id")
                        val id: String = "",
                        @SerializedName("media_url")
                        val mediaUrl: String = "",
                        @SerializedName("title")
                        val title: String = "",
                        @SerializedName("url")
                        val url: String = ""
                    )
                }
            }
        }
    }
}