package com.tokopedia.product.addedit.detail.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PriceSuggestionSuggestedPriceGetResponse (
        @SerializedName("PriceSuggestionSuggestedPriceGet")
        @Expose
        val priceSuggestionSuggestedPriceGet: PriceSuggestionSuggestedPriceGet = PriceSuggestionSuggestedPriceGet()
)

data class PriceSuggestionSuggestedPriceGetByKeywordResponse (
        @SerializedName("PriceSuggestionSuggestedPriceGetByKeyword")
        @Expose
        val priceSuggestionSuggestedPriceGet: PriceSuggestionSuggestedPriceGet = PriceSuggestionSuggestedPriceGet()
)

data class PriceSuggestionSuggestedPriceGet (
        @SerializedName("suggestedPrice")
        @Expose
        val suggestedPrice: Double = 0.0,

        @SerializedName("suggestedPriceMax")
        @Expose
        val suggestedPriceMax: Double = 0.0,

        @SerializedName("suggestedPriceMin")
        @Expose
        val suggestedPriceMin: Double = 0.0,

        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        @Expose
        val price: Double = 0.0,

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("productRecommendation")
        @Expose
        val productRecommendation: List<ProductRecommendationResponse> = listOf()
)

data class ProductRecommendationResponse(
        @SerializedName("productID")
        val productID: String? = "",
        @SerializedName("price")
        val price: Double? = 0.0,
        @SerializedName("imageURL")
        val imageURL: String? = "",
        @SerializedName("sold")
        val sold: Int? = 0,
        @SerializedName("rating")
        val rating: Double? = 0.0,
        @SerializedName("title")
        val title: String? = ""
)
