package com.tokopedia.tokofood.feature.merchant.domain.model.response

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetMerchantDataResponse(
        @SerializedName("tokofoodGetMerchantData")
        @Expose val tokofoodGetMerchantData: TokoFoodGetMerchantData,
)

data class TokoFoodGetMerchantData(
        @SerializedName("ticker")
        @Expose val ticker: TokoFoodTickerDetail,
        @SerializedName("merchantProfile")
        @Expose val merchantProfile: TokoFoodMerchantProfile,
        @SerializedName("filters")
        @Expose val filters: List<TokoFoodCategoryFilter>,
        @SerializedName("categories")
        @Expose val categories: List<TokoFoodCategoryCatalog>
)

data class TokoFoodTickerDetail(
        @SerializedName("title")
        @Expose val title: String = "",
        @SerializedName("subtitle")
        @Expose val subtitle: String = "",
        @SerializedName("link")
        @Expose val link: String = "",
        @SerializedName("type")
        @Expose val type: String = ""
)

data class TokoFoodMerchantProfile(
        @SerializedName("name")
        @Expose val name: String = "",
        @SerializedName("address")
        @Expose val address: String = "",
        @SerializedName("latitude")
        @Expose val latitude: String = "",
        @SerializedName("longitude")
        @Expose val longitude: String = "",
        @SerializedName("imageURL")
        @Expose val imageURL: String? = "",
        @SerializedName("closeWarning")
        @Expose val closeWarning: String? = "",
        @SerializedName("merchantCategories")
        @Expose val merchantCategories: List<String> = listOf(),
        @SerializedName("rating")
        @Expose val rating: String = "",
        @SerializedName("ratingFmt")
        @Expose val ratingFmt: String = "",
        @SerializedName("totalRating")
        @Expose val totalRating: Double = 0.0,
        @SerializedName("totalRatingFmt")
        @Expose val totalRatingFmt: String = "",
        @SerializedName("distance")
        @Expose val distance: Double = 0.0,
        @SerializedName("distanceFmt")
        @Expose val distanceFmt: TokoFoodFmtWarningContent = TokoFoodFmtWarningContent(),
        @SerializedName("deliverable")
        @Expose val deliverable: Boolean = false,
        @SerializedName("eta")
        @Expose val eta: Int = 0,
        @SerializedName("etaFmt")
        @Expose val etaFmt: TokoFoodFmtWarningContent = TokoFoodFmtWarningContent(),
        @SerializedName("opsHourFmt")
        @Expose val opsHourFmt: TokoFoodFmtWarningContent = TokoFoodFmtWarningContent(),
        @SerializedName("opsHourDetail")
        @Expose val opsHourDetail: List<TokoFoodMerchantOpsHour>
)

data class TokoFoodFmtWarningContent(
        @SerializedName("content")
        @Expose val content: String = "",
        @SerializedName("isWarning")
        @Expose val isWarning: Boolean = false
)

data class TokoFoodMerchantOpsHour(
        @SerializedName("day")
        @Expose val day: String = "",
        @SerializedName("time")
        @Expose val time: String = "",
        @SerializedName("isWarning")
        @Expose val isWarning: Boolean = false
)

data class TokoFoodCategoryFilter(
        @SerializedName("key")
        @Expose val key: String = "",
        @SerializedName("title")
        @Expose val title: String = "",
        @SerializedName("subtitle")
        @Expose val subtitle: String = ""
)

@SuppressLint("Invalid Data Type")
data class TokoFoodCatalogDetail(
        @SerializedName("id")
        @Expose val id: String = "",
        @SerializedName("name")
        @Expose val name: String = "",
        @SerializedName("description")
        @Expose val description: String = "",
        @SerializedName("imageURL")
        @Expose val imageURL: String = "",
        @SerializedName("price")
        @Expose val price: Double = 0.0,
        @SerializedName("priceFmt")
        @Expose val priceFmt: String = "",
        @SerializedName("slashPrice")
        @Expose val slashPrice: Double = 0.0,
        @SerializedName("slashPriceFmt")
        @Expose val slashPriceFmt: String = "",
        @SerializedName("isOutOfStock")
        @Expose val isOutOfStock: Boolean = false,
        @SerializedName("variants")
        @Expose val variants: List<TokoFoodCatalogVariantDetail>
)

@Parcelize
data class TokoFoodCatalogVariantDetail(
        @SerializedName("id")
        @Expose val id: String = "",
        @SerializedName("name")
        @Expose val name: String = "",
        @SerializedName("options")
        @Expose val options: List<TokoFoodCatalogVariantOptionDetail>,
        @SerializedName("isRequired")
        @Expose val isRequired: Boolean = false,
        @SerializedName("maxQty")
        @Expose val maxQty: Int = 0,
        @SerializedName("minQty")
        @Expose val minQty: Int = 0
) : Parcelable

@SuppressLint("Invalid Data Type")
@Parcelize
data class TokoFoodCatalogVariantOptionDetail(
        @SerializedName("id")
        @Expose val id: String = "",
        @SerializedName("name")
        @Expose val name: String = "",
        @SerializedName("price")
        @Expose val price: Double = 0.0,
        @SerializedName("priceFmt")
        @Expose val priceFmt: String = "",
        @SerializedName("status")
        @Expose val status: Int
) : Parcelable

data class TokoFoodCategoryCatalog(
        @SerializedName("id")
        @Expose val id: String = "",
        @SerializedName("key")
        @Expose val key: String = "",
        @SerializedName("categoryName")
        @Expose val categoryName: String = "",
        @SerializedName("catalogs")
        @Expose val catalogs: List<TokoFoodCatalogDetail>
)