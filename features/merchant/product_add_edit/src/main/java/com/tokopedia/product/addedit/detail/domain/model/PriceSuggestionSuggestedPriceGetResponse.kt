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
        val title: String = ""
)
