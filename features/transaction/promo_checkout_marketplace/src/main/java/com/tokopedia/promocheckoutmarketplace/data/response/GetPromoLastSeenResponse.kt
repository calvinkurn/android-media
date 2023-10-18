package com.tokopedia.promocheckoutmarketplace.data.response

import com.google.gson.annotations.SerializedName

data class GetPromoSuggestionResponse(
    @SerializedName("GetPromoSuggestion")
    val promoSuggestion: GetPromoSuggestion = GetPromoSuggestion()
)

data class GetPromoSuggestion(
    @SerializedName("PromoHistory")
    val promoHistory: List<PromoHistory> = emptyList()
)

data class PromoHistory(
    @SerializedName("PromoCode")
    val promoCode: String = "",
    @SerializedName("PromoContent")
    val promoContent: PromoContent = PromoContent()
)

data class PromoContent(
    @SerializedName("PromoTitle")
    val promoTitle: String = ""
)
