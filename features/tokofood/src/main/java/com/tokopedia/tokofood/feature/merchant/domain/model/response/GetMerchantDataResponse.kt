package com.tokopedia.tokofood.feature.merchant.domain.model.response

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetMerchantDataResponse(
        @SerializedName("tokofoodGetMerchantData")
        val tokofoodGetMerchantData: TokoFoodGetMerchantData
)

data class TokoFoodGetMerchantData(
        @SerializedName("ticker")
        val ticker: TokoFoodTickerDetail,
        @SerializedName("topBanner")
        val topBanner: TokoFoodTopBanner,
        @SerializedName("merchantProfile")
        val merchantProfile: TokoFoodMerchantProfile,
        @SerializedName("filters")
        val filters: List<TokoFoodCategoryFilter>,
        @SerializedName("categories")
        val categories: List<TokoFoodCategoryCatalog>
)

data class TokoFoodTickerDetail(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("subtitle")
        val subtitle: String = "",
        @SerializedName("link")
        val link: String = "",
        @SerializedName("type")
        val type: String = ""
)

data class TokoFoodTopBanner(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("subtitle")
        val subtitle: String = "",
        @SerializedName("imageURL")
        val imageUrl: String = "",
        @SerializedName("isShown")
        val isShown: Boolean = false
)

data class TokoFoodMerchantProfile(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("address")
        val address: String = "",
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("imageURL")
        val imageURL: String? = "",
        @SerializedName("closeWarning")
        val closeWarning: String? = "",
        @SerializedName("merchantCategories")
        val merchantCategories: List<String> = listOf(),
        @SerializedName("rating")
        val rating: String = "",
        @SerializedName("ratingFmt")
        val ratingFmt: String = "",
        @SerializedName("totalRating")
        val totalRating: Double = 0.0,
        @SerializedName("totalRatingFmt")
        val totalRatingFmt: String = "",
        @SerializedName("distance")
        val distance: Double = 0.0,
        @SerializedName("distanceFmt")
        val distanceFmt: TokoFoodFmtWarningContent = TokoFoodFmtWarningContent(),
        @SerializedName("deliverable")
        val deliverable: Boolean = false,
        @SerializedName("eta")
        val eta: Int = 0,
        @SerializedName("etaFmt")
        val etaFmt: TokoFoodFmtWarningContent = TokoFoodFmtWarningContent(),
        @SerializedName("opsHourFmt")
        val opsHourFmt: TokoFoodFmtWarningContent = TokoFoodFmtWarningContent(),
        @SerializedName("opsHourDetail")
        val opsHourDetail: List<TokoFoodMerchantOpsHour> = listOf()
)

data class TokoFoodFmtWarningContent(
        @SerializedName("content")
        val content: String = "",
        @SerializedName("isWarning")
        val isWarning: Boolean = false
)

data class TokoFoodMerchantOpsHour(
        @SerializedName("day")
        val day: String = "",
        @SerializedName("time")
        val time: String = "",
        @SerializedName("isWarning")
        val isWarning: Boolean = false
)

data class TokoFoodCategoryFilter(
        @SerializedName("key")
        val key: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("subtitle")
        val subtitle: String = ""
)

@SuppressLint("Invalid Data Type")
data class TokoFoodCatalogDetail(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("imageURL")
        val imageURL: String = "",
        @SerializedName("price")
        val price: Double = 0.0,
        @SerializedName("priceFmt")
        val priceFmt: String = "",
        @SerializedName("slashPrice")
        val slashPrice: Double = 0.0,
        @SerializedName("slashPriceFmt")
        val slashPriceFmt: String = "",
        @SerializedName("isOutOfStock")
        val isOutOfStock: Boolean = false,
        @SerializedName("variants")
        val variants: List<TokoFoodCatalogVariantDetail>
)

@Parcelize
data class TokoFoodCatalogVariantDetail(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("options")
        val options: List<TokoFoodCatalogVariantOptionDetail>,
        @SerializedName("isRequired")
        val isRequired: Boolean = false,
        @SerializedName("maxQty")
        val maxQty: Int = 0,
        @SerializedName("minQty")
        val minQty: Int = 0
) : Parcelable

@SuppressLint("Invalid Data Type")
@Parcelize
data class TokoFoodCatalogVariantOptionDetail(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("price")
        val price: Double = 0.0,
        @SerializedName("priceFmt")
        val priceFmt: String = "",
        @SerializedName("status")
        val status: Int
) : Parcelable

data class TokoFoodCategoryCatalog(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("key")
        val key: String = "",
        @SerializedName("categoryName")
        val categoryName: String = "",
        @SerializedName("catalogs")
        val catalogs: List<TokoFoodCatalogDetail> = listOf()
)
