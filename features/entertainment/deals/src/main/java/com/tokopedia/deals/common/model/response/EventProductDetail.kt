package com.tokopedia.deals.common.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.android.parcel.Parcelize

/**
 * @author by jessica on 18/06/20
 */

@Parcelize
data class EventProductDetail(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("brand_id")
        @Expose
        val brandId: String = "",

        @SerializedName("category_id")
        @Expose
        val categoryId: String = "",

        @SerializedName("provider_id")
        @Expose
        val providerId: String = "",

        @SerializedName("child_category_ids")
        @Expose
        val childCategoryIds: String = "",

        @SerializedName("city_ids")
        @Expose
        val cityIds: String = "",

        @SerializedName("display_name")
        @Expose
        val displayName: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("seo_url")
        @Expose
        val seoUrl: String = "",

        @SerializedName("image_web")
        @Expose
        val imageWeb: String = "",

        @SerializedName("thumbnail_web")
        @Expose
        val thumbnailWeb: String = "",

        @SerializedName("image_app")
        @Expose
        val imageApp: String = "",

        @SerializedName("thumbnail_app")
        @Expose
        val thumbnailApp: String = "",

        @SerializedName("meta_title")
        @Expose
        val metaTitle: String = "",

        @SerializedName("meta_description")
        @Expose
        val metaDescription: String = "",

        @SerializedName("meta_keywords")
        @Expose
        val metaKeywords: String = "",

        @SerializedName("display_tags")
        @Expose
        val displayTags: String = "",

        @SerializedName("mrp")
        @Expose
        val mrp: String = "",

        @SerializedName("sales_price")
        @Expose
        val salesPrice: String = "",

        @SerializedName("priority")
        @Expose
        val priority: String = "",

        @SerializedName("is_featured")
        @Expose
        val isFeatured: Int = 0,

        @SerializedName("id_promo")
        @Expose
        val isPromo: String = "0",

        @SerializedName("is_food_available")
        @Expose
        val isFoodAvailable: Int = 0,

        @SerializedName("is_searchable")
        @Expose
        val isSearchable: Int = 0,

        @SerializedName("is_top")
        @Expose
        val isTop: Int = 0,

        @SerializedName("status")
        @Expose
        val status: Int = 0,

        @SerializedName("min_start_date")
        @Expose
        val minStartDate: String = "",

        @SerializedName("max_end_date")
        @Expose
        val maxEndDate: String = "",

        @SerializedName("sale_start_date")
        @Expose
        val salesStartDate: String = "",

        @SerializedName("sale_end_date")
        @Expose
        val salesEndDate: String = "",

        @SerializedName("custom_labels")
        @Expose
        val customLabels: String = "",

        @SerializedName("custom_text_1")
        @Expose
        val customText1: String = "",

        @SerializedName("city_name")
        @Expose
        val cityName: String = "",

        @SerializedName("rating")
        @Expose
        val rating: String = "",

        @SerializedName("likes")
        @Expose
        val likes: Int = 0,

        @SerializedName("is_liked")
        @Expose
        val isLiked: Boolean = false,

        @SerializedName("saving")
        @Expose
        val saving: String = "",

        @SerializedName("saving_percentage")
        @Expose
        val savingPercentage: String = "",

        @SerializedName("location")
        @Expose
        val location: String = "",

        @SerializedName("web_url")
        @Expose
        val webUrl: String = "",

        @SerializedName("app_url")
        @Expose
        val appUrl: String = "",

        @SerializedName("brand")
        @Expose
        val brand: Brand = Brand(),

        @SerializedName("category")
        @Expose
        val category: List<Category> = listOf()
) : Parcelable, ImpressHolder()

@Parcelize
data class Brand(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("seo_url")
        @Expose
        val seoUrl: String = "",

        @SerializedName("featured_image")
        @Expose
        val featuredImage: String = "",

        @SerializedName("featured_thumbnail_image")
        @Expose
        val featuredThumbnailImage: String = "",

        @SerializedName("city_name")
        @Expose
        val cityName: String = ""
): Parcelable

@Parcelize
data class Category(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("title")
        @Expose
        val  title: String = "",

        @SerializedName("media_url")
        @Expose
        val mediaUrl: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
): Parcelable
