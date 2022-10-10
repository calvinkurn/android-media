package com.tokopedia.product.addedit.detail.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetAddProductPriceSuggestionResponse(
        @SerializedName("PriceSuggestionSuggestedPriceGetByKeywordV2")
        val priceSuggestionByKeyword: PriceSuggestionByKeyword
)

@Parcelize
data class PriceSuggestionByKeyword(
        @SerializedName("status")
        val status: String? = "",
        @SerializedName("summary")
        val summary: PriceSuggestionSuggestedPriceByKeywordSummary? = PriceSuggestionSuggestedPriceByKeywordSummary(),
        @SerializedName("suggestions")
        val suggestions: List<PriceSuggestionSuggestedPriceGetResponseV2>? = listOf()
) : Parcelable

@Parcelize
data class PriceSuggestionSuggestedPriceByKeywordSummary(
        @SerializedName("suggestedPrice")
        val suggestedPrice: Double? = 0.0,
        @SerializedName("suggestedPriceMin")
        val suggestedPriceMin: Double? = 0.0,
        @SerializedName("suggestedPriceMax")
        val suggestedPriceMax: Double? = 0.0
) : Parcelable

@Parcelize
data class PriceSuggestionSuggestedPriceGetResponseV2(
        @SerializedName("productId")
        val productId: String? = "",
        @SerializedName("displayPrice")
        val displayPrice: Double? = 0.0,
        @SerializedName("imageURL")
        val imageURL: String? = "",
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("totalSold")
        val totalSold: Int? = 0,
        @SerializedName("rating")
        val rating: Double? = 0.0
) : Parcelable