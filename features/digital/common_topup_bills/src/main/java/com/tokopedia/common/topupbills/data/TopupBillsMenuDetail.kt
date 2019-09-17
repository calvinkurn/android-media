package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 28/05/19.
 */
class TopupBillsMenuDetail(
        @SerializedName("catalog")
        @Expose
        val catalog: TopupBillsCatalog,
        @SerializedName("recommendations")
        @Expose
        val recommendations: List<TopupBillsRecommendation>,
        @SerializedName("promos")
        @Expose
        val promos: List<TopupBillsPromo>,
        @SerializedName("tickers")
        @Expose
        val tickers: List<TopupBillsTicker>,
        @SerializedName("banners")
        @Expose
        val banners: List<TopupBillsBanner>
)