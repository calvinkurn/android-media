package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 28/05/19.
 */
class TopupBillsMenuDetail(
        @SerializedName("catalog")
        @Expose
        val catalog: TopupBillsCatalog = TopupBillsCatalog(),
        @SerializedName("recommendations")
        @Expose
        val recommendations: List<TopupBillsRecommendation> = listOf(),
        @SerializedName("promos")
        @Expose
        val promos: List<TopupBillsPromo> = listOf(),
        @SerializedName("tickers")
        @Expose
        val tickers: List<TopupBillsTicker> = listOf(),
        @SerializedName("banners")
        @Expose
        val banners: List<TopupBillsBanner> = listOf(),
        @SerializedName("onboardingDescriptions")
        @Expose
        val onboardingDescriptions: List<String> = listOf(),
        @SerializedName("express_checkout")
        @Expose
        val isExpressCheckout: Boolean = false
)