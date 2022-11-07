package com.tokopedia.common_entertainment.data

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DealsDetailsResponse (
    @SerializedName("id")
    var id: Long = 0,
    @SerializedName("brand_id")
    var brandId: Long = 0,
    @SerializedName("category_id")
    var categoryId: Long = 0,
    @SerializedName("provider_id")
    var providerId: Long = 0,
    @SerializedName("provider_product_id")
    var providerProductId: String = "",
    @SerializedName("provider_product_name")
    var providerProductName: String = "",
    @SerializedName("display_name")
    var displayName: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("image_web")
    var imageWeb: String = "",
    @SerializedName("thumbnail_web")
    var thumbnailWeb: String = "",
    @SerializedName("long_rich_desc")
    var longRichDesc: String = "",
    @SerializedName("mrp")
    var mrp: Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("sales_price")
    var salesPrice: Long = 0,
    @SerializedName("quantity")
    var quantity: Int = 0,
    @SerializedName("sold_quantity")
    var soldQuantity: Int = 0,
    @SerializedName("sell_rate")
    var sellRate: Int = 0,
    @SerializedName("thumbs_up")
    var thumbsUp: Int = 0,
    @SerializedName("thumbs_down")
    var thumbsDown: Int = 0,
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("min_start_date")
    var minStartDate: Int = 0,
    @SerializedName("max_end_date")
    var maxEndDate: Int = 0,
    @SerializedName("sale_start_date")
    var saleStartDate: Int = 0,
    @SerializedName("sale_end_date")
    var saleEndDate: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("min_qty")
    var minQty: Int = 0,
    @SerializedName("max_qty")
    var maxQty: Int = 0,
    @SerializedName("min_start_time")
    var minStartTime: String = "",
    @SerializedName("max_end_time")
    var maxEndTime: String = "",
    @SerializedName("sale_start_time")
    var saleStartTime: String = "",
    @SerializedName("sale_end_time")
    var saleEndTime: String = "",
    @SerializedName("date_range")
    var dateRange: Boolean = false,
    @SerializedName("city_name")
    var cityName: String = "",
    @SerializedName("outlets")
    var outlets: List<Outlet>? = null,
    @SerializedName("rating")
    var rating: Int = 0,
    @SerializedName("likes")
    var likes: Int = 0,
    @SerializedName("catalog")
    var catalog: Catalog = Catalog(),
    @SerializedName("saving_percentage")
    var savingPercentage: String = "",
    @SerializedName("brand")
    var brand: Brand = Brand(),
    @SerializedName("recommendation_url")
    var recommendationUrl: String = "",
    @SerializedName("media")
    var mediaUrl: List<Media>? = null,
    @SerializedName("tnc")
    var tnc: String = "",
    @SerializedName("seo_url")
    var seoUrl: String = "",
    @SerializedName("is_liked")
    var isLiked: Boolean = false,
    @SerializedName("desktop_url")
    var desktopUrl: String = "",
    @SerializedName("web_url")
    var webUrl: String = "",
    @SerializedName("app_url")
    var appUrl: String = "",
    @SerializedName("custom_text_1")
    var customText1: Int = 0,
    @SerializedName("checkout_business_type")
    var checkoutBusinessType: Int = 0,
    @SerializedName("checkout_data_type")
    var checkoutDataType: String? = null
) : Parcelable

@Parcelize
data class Media (
    @SerializedName("id")
    var id: String = "",
    @SerializedName("product_id")
    var productId: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("is_thumbnail")
    var isThumbnail: Int = 0,
    @SerializedName("type")
    var type: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("client")
    var client: String = "",
    @SerializedName("status")
    var status: Int = 0
) : Parcelable

@Parcelize
data class Outlet (
    @SerializedName("id")
    var id: String = "",
    @SerializedName("product_id")
    var productId: String = "",
    @SerializedName("location_id")
    var locationId: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("search_name")
    var searchName: String = "",
    @SerializedName("meta_title")
    var metaTitle: String = "",
    @SerializedName("meta_description")
    var metaDescription: String = "",
    @SerializedName("meta_keywords")
    var metaKeywords: String = "",
    @SerializedName("district")
    var district: String = "",
    @SerializedName("gmap_address")
    var gmapAddress: String = "",
    @SerializedName("neighbourhood")
    var neighbourhood: String = "",
    @SerializedName("coordinates")
    var coordinates: String = "",
    @SerializedName("state")
    var state: String = "",
    @SerializedName("country")
    var country: String = "",
    @SerializedName("is_searchable")
    var isSearchable: Int = 0,
    @SerializedName("location_status")
    var locationStatus: Int = 0
) : Parcelable

@Parcelize
data class Catalog (
    @SerializedName("digital_category_id")
    var digitalCategoryId: String = "",
    @SerializedName("digital_product_id")
    var digitalProductId: String = "",
    @SerializedName("digital_product_code")
    var digitalProductCode: String = ""
) : Parcelable

@Parcelize
data class Brand (
    @SerializedName("id") 
    var id: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("description") 
    var description: String = "", 
    @SerializedName("url") 
    var url: String = "",
    @SerializedName("seo_url") 
    var seoUrl: String = "",
    @SerializedName("featured_image")
    var featuredImage: String = "",
    @SerializedName("featured_thumbnail_image") 
    var featuredThumbnailImage: String = "",
    @SerializedName("city_name") 
    var cityName: String = ""
) : Parcelable
