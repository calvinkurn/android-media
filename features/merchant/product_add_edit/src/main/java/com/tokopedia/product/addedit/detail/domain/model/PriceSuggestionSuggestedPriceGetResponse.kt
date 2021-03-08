package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.lang.Double

data class PriceSuggestionSuggestedPriceGetResponse (
        @SerializedName("PriceSuggestionSuggestedPriceGet")
        @Expose
        val priceSuggestionSuggestedPriceGet: PriceSuggestionSuggestedPriceGet = PriceSuggestionSuggestedPriceGet()
)

data class PriceSuggestionSuggestedPriceGet (
        @SerializedName("suggestedPrice")
        @Expose
        val suggestedPrice: Double = Double(0.0),

        @SerializedName("suggestedPriceMax")
        @Expose
        val suggestedPriceMax: Double = Double(0.0),

        @SerializedName("suggestedPriceMin")
        @Expose
        val suggestedPriceMin: Double = Double(0.0),

        @SerializedName("price")
        @Expose
        val price: Double = Double(0.0)
)
