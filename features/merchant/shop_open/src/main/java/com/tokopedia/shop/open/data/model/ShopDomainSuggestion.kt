package com.tokopedia.shop.open.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopDomainSuggestionResult(
        @SerializedName("shopDomainSuggestion")
        @Expose
        val shopDomainSuggestion: ShopDomainSuggestion = ShopDomainSuggestion()
)
data class ShopDomainSuggestion(
        @SerializedName("result")
        @Expose
        val result: ValidateShopDomainSuggestionResult = ValidateShopDomainSuggestionResult(),
        @SerializedName("error")
        @Expose
        val error: ValidateShopDomainSuggestionError = ValidateShopDomainSuggestionError()
)

data class ValidateShopDomainSuggestionResult(
        @SerializedName("shopDomain")
        @Expose
        val shopDomain: String = "",
        @SerializedName("shopDomains")
        @Expose
        val shopDomains: List<String> = listOf()
)

data class ValidateShopDomainSuggestionError(
        @SerializedName("message")
        @Expose
        val message: String = ""
)
