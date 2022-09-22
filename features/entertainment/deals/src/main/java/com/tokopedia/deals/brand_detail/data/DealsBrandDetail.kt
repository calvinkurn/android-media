package com.tokopedia.deals.brand_detail.data

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder

data class DealsBrandDetail(
        @SerializedName("event_brand_detail_v2")
        @Expose
        val data : DealsBrandDetailV2 = DealsBrandDetailV2()
)

data class DealsBrandDetailV2(
        @SerializedName("brand")
        @Expose
        val brand : Brand = Brand(),
        @SerializedName("products")
        @Expose
        val products : List<Product> = emptyList()
)

data class Brand(
        @SerializedName("id")
        @Expose
        val id : String = "",
        @SerializedName("title")
        @Expose
        val title : String = "",
        @SerializedName("description")
        @Expose
        val description : String = "",
        @SerializedName("url")
        @Expose
        val url : String = "",
        @SerializedName("seo_url")
        @Expose
        val seoUrl : String = "",
        @SerializedName("featured_image")
        @Expose
        val featuredImage : String = "",
        @SerializedName("featured_thumbnail_image")
        @Expose
        val featuredThumbnailImage : String = "",
        @SerializedName("city_name")
        @Expose
        val cityName : String = "",
)

data class Product(
        @SerializedName("id")
        @Expose
        val id : String = "",
        @SerializedName("brand_id")
        @Expose
        val brandId : String = "",
        @SerializedName("category_id")
        @Expose
        val categoryId : String = "",
        @SerializedName("provider_id")
        @Expose
        val providerId : String = "",
        @SerializedName("child_category_ids")
        @Expose
        val childCategoryIds : String = "",
        @SerializedName("city_ids")
        @Expose
        val cityIds : String = "",
        @SerializedName("display_name")
        @Expose
        val displayName : String = "",
        @SerializedName("title")
        @Expose
        val title : String = "",
        @SerializedName("url")
        @Expose
        val url : String = "",
        @SerializedName("seo_url")
        @Expose
        val seoUrl : String = "",
        @SerializedName("image_web")
        @Expose
        val imageWeb : String = "",
        @SerializedName("thumbnail_web")
        @Expose
        val thumbnailWeb : String = "",
        @SerializedName("image_app")
        @Expose
        val imageApp : String = "",
        @SerializedName("thumbnail_app")
        @Expose
        val thumbnailApp : String = "",
        @SerializedName("display_tags")
        @Expose
        val displayTags : String = "",
        @SerializedName("mrp")
        @Expose
        val mrp : Long = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("sales_price")
        @Expose
        val salesPrice : Long = 0,
        @SerializedName("priority")
        @Expose
        val priority : Int = 0,
        @SerializedName("is_searchable")
        @Expose
        val isSearchable : Int = 0,
        @SerializedName("status")
        @Expose
        val status : Int = 0,
        @SerializedName("max_end_date")
        @Expose
        val maxEndDate : Int = 0,
        @SerializedName("min_start_date")
        @Expose
        val minStartDate : Int = 0,
        @SerializedName("sale_end_date")
        @Expose
        val saleEndDate : Int = 0,
        @SerializedName("sale_start_date")
        @Expose
        val saleStartDate : Int = 0,
        @SerializedName("custom_text_1")
        @Expose
        val customText1 : Int = 0,
        @SerializedName("min_start_time")
        @Expose
        val minStartTime : String = "",
        @SerializedName("max_end_time")
        @Expose
        val maxEndTime : String = "",
        @SerializedName("sale_end_time")
        @Expose
        val saleEndTime : String = "",
        @SerializedName("sale_start_time")
        @Expose
        val saleStartTime : String = "",
        @SerializedName("city_name")
        @Expose
        val cityName : String = "",
        @SerializedName("likes")
        @Expose
        val likes : Int = 0,
        @SerializedName("is_liked")
        @Expose
        val isLiked : Boolean,
        @SerializedName("saving_percentage")
        @Expose
        val savingPercentage : String = "",
        @SerializedName("category")
        @Expose
        val category : List<Category>,
        @SerializedName("message")
        @Expose
        val message : String = "",
        @SerializedName("code")
        @Expose
        val code : String = "",
        @SerializedName("message_error")
        @Expose
        val messageError : String = "",
        @SerializedName("no_promo")
        @Expose
        val noPromo : Boolean = false,
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        @Expose
        val price : String = "",
        @SerializedName("location")
        @Expose
        val location : String = "",
        @SerializedName("schedule")
        @Expose
        val schedule : String = "",
        @SerializedName("web_url")
        @Expose
        val webUrl : String = "",
        @SerializedName("app_url")
        @Expose
        val appUrl : String = ""
): ImpressHolder()

data class Category (
        @SerializedName("id")
        @Expose
        val id : String = "",
        @SerializedName("title")
        @Expose
        val title : String = "",
        @SerializedName("media_url")
        @Expose
        val mediaUrl : String = "",
        @SerializedName("url")
        @Expose
        val url : String = ""
)