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
        @SerializedName("productId")
        @Expose
        val productId: String = "",

        @SerializedName("shopId")
        @Expose
        val shopId: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

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
        val price: Double = Double(0.0),

        @SerializedName("slashPrice")
        @Expose
        val slashPrice: Double = Double(0.0),

        @SerializedName("processStep")
        @Expose
        val processStep: String = "",

        @SerializedName("productStatus")
        @Expose
        val productStatus: String = "",

        @SerializedName("updateTime")
        @Expose
        val updateTime: String = ""
)
