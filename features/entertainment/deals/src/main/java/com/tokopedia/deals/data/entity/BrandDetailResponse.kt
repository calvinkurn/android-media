package com.tokopedia.deals.data.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder

data class DealsBrandDetail(
    @SerializedName("event_brand_detail_v2")
    val data: DealsBrandDetailV2 = DealsBrandDetailV2()
)

data class DealsBrandDetailV2(
    @SerializedName("brand")
    val brand: Brand = Brand(),
    @SerializedName("products")
    val products: List<Product> = emptyList()
)

data class Brand(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("seo_url")
    val seoUrl: String = "",
    @SerializedName("featured_image")
    val featuredImage: String = "",
    @SerializedName("featured_thumbnail_image")
    val featuredThumbnailImage: String = "",
    @SerializedName("city_name")
    val cityName: String = ""
)

data class Product(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("brand_id")
    val brandId: String = "",
    @SerializedName("category_id")
    val categoryId: String = "",
    @SerializedName("provider_id")
    val providerId: String = "",
    @SerializedName("child_category_ids")
    val childCategoryIds: String = "",
    @SerializedName("city_ids")
    val cityIds: String = "",
    @SerializedName("display_name")
    val displayName: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("seo_url")
    val seoUrl: String = "",
    @SerializedName("image_web")
    val imageWeb: String = "",
    @SerializedName("thumbnail_web")
    val thumbnailWeb: String = "",
    @SerializedName("image_app")
    val imageApp: String = "",
    @SerializedName("thumbnail_app")
    val thumbnailApp: String = "",
    @SerializedName("display_tags")
    val displayTags: String = "",
    @SerializedName("mrp")
    val mrp: Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("sales_price")
    val salesPrice: Long = 0,
    @SerializedName("priority")
    val priority: Int = 0,
    @SerializedName("is_searchable")
    val isSearchable: Int = 0,
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("max_end_date")
    val maxEndDate: Int = 0,
    @SerializedName("min_start_date")
    val minStartDate: Int = 0,
    @SerializedName("sale_end_date")
    val saleEndDate: Int = 0,
    @SerializedName("sale_start_date")
    val saleStartDate: Int = 0,
    @SerializedName("custom_text_1")
    val customText1: Int = 0,
    @SerializedName("min_start_time")
    val minStartTime: String = "",
    @SerializedName("max_end_time")
    val maxEndTime: String = "",
    @SerializedName("sale_end_time")
    val saleEndTime: String = "",
    @SerializedName("sale_start_time")
    val saleStartTime: String = "",
    @SerializedName("city_name")
    val cityName: String = "",
    @SerializedName("likes")
    val likes: Int = 0,
    @SerializedName("is_liked")
    val isLiked: Boolean,
    @SerializedName("saving_percentage")
    val savingPercentage: String = "",
    @SerializedName("category")
    val category: List<Category>,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message_error")
    val messageError: String = "",
    @SerializedName("no_promo")
    val noPromo: Boolean = false,
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: String = "",
    @SerializedName("location")
    val location: String = "",
    @SerializedName("schedule")
    val schedule: String = "",
    @SerializedName("web_url")
    val webUrl: String = "",
    @SerializedName("app_url")
    val appUrl: String = ""
) : ImpressHolder() {
    data class Category(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("media_url")
        val mediaUrl: String = "",
        @SerializedName("url")
        val url: String = ""
    )
}


