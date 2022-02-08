package com.tokopedia.tradein.model


import com.google.gson.annotations.SerializedName

data class PromoTradeInModel(
    @SerializedName("tradeInPromoDetail")
    var tradeInPromoDetail: TradeInPromoDetail
) {
    data class TradeInPromoDetail(
        @SerializedName("BenefitFmt")
        var benefitFmt: String,
        @SerializedName("Code")
        var code: String,
        @SerializedName("ConditionFmt")
        var conditionFmt: String,
        @SerializedName("ImageURL")
        var imageURL: String,
        @SerializedName("PeriodFmt")
        var periodFmt: String,
        @SerializedName("TermsConditions")
        var termsConditions: String,
        @SerializedName("Title")
        var title: String
    )
}