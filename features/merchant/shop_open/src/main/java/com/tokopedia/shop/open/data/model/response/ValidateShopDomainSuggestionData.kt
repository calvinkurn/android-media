package com.tokopedia.shop.open.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ShopDomainSuggestionHeader(
        @SerializedName("shopDomainSuggestion")
        @Expose
        val shopDomainSuggestion:ShopDomainSuggestion = ShopDomainSuggestion()
)
data class ShopDomainSuggestion(
        @SerializedName("result")
        @Expose
        val result: ValidateShopDomainSuggestionResult = ValidateShopDomainSuggestionResult(),
        @SerializedName("error")
        @Expose
        val error:ValidateShopDomainSuggestionError = ValidateShopDomainSuggestionError()
)

data class ValidateShopDomainSuggestionResult(
        @SerializedName("shopDomain")
        @Expose
        val shopDomain:String = ""
)

data class ValidateShopDomainSuggestionError(
        @SerializedName("message")
        @Expose
        val message:String = ""
)