package com.tokopedia.tokofood.common.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder

data class TokoFoodMerchantList(
    @SerializedName("merchants")
    val merchants: List<Merchant> = emptyList(),
    @SerializedName("nextPageKey")
    val nextPageKey: String = "",
)

data class Merchant (
    @SerializedName("id")
    val id: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("brandID")
    val brandId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("merchantCategories")
    val merchantCategories: List<String> = emptyList(),
    @SerializedName("imageURL")
    val imageURL: String = "",
    @SerializedName("priceLevel")
    val priceLevel: PriceLevel = PriceLevel(),
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
    @SerializedName("branchApplink")
    val branchApplink: String = "",
    @SerializedName("isClosed")
    val isClosed: Boolean = false,
    @SerializedName("addressLocality")
    val addressLocality: String = ""
): ImpressHolder()

data class PriceLevel(
    @SerializedName("icon")
    val icon: String = "$",
    @SerializedName("fareCount")
    val fareCount: Int = 0,
)