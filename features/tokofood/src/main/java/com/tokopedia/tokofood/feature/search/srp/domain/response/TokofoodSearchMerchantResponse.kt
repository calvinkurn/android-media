package com.tokopedia.tokofood.feature.search.srp.domain.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder

data class TokofoodSearchMerchantResponse(
    @SerializedName("tokofoodSearchMerchant")
    val tokofoodSearchMerchant: TokofoodSearchMerchant = TokofoodSearchMerchant()
)

data class TokofoodSearchMerchant(
    @SerializedName("merchants")
    val merchants: List<TokofoodSearchMerchantItem> = emptyList(),
    @SerializedName("nextPageKey")
    val nextPageKey: String = "",
)

data class TokofoodSearchMerchantItem (
    @SerializedName("id")
    val id: String = "",
    @SerializedName("brandID")
    val brandId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("merchantCategories")
    val merchantCategories: List<String> = emptyList(),
    @SerializedName("imageURL")
    val imageURL: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("priceLevel")
    val priceLevel: TokofoodSearchPriceLevel = TokofoodSearchPriceLevel(),
    @SerializedName("rating")
    val rating: Double = 0.0,
    @SerializedName("ratingFmt")
    val ratingFmt: String = "",
    @SerializedName("distance")
    val distance: Double = 0.0,
    @SerializedName("distanceFmt")
    val distanceFmt: String = "",
    @SerializedName("etaFmt")
    val etaFmt: String = "",
    @SerializedName("promo")
    val promo: String = "",
    @SerializedName("hasBranch")
    val hasBranch: Boolean = false,
    @SerializedName("isClosed")
    val isClosed: Boolean = false,
    @SerializedName("addressLocality")
    val addressLocality: String = ""
): ImpressHolder()

data class TokofoodSearchPriceLevel(
    @SerializedName("icon")
    val icon: String = "$",
    @SerializedName("fareCount")
    val fareCount: Int = 0,
)