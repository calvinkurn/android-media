package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 28/05/19.
 */
class TelcoCatalogMenuDetail(
        @SerializedName("recommendations")
        @Expose
        val recommendations: List<TelcoRecommendation>,
        @SerializedName("promos")
        @Expose
        val promos: List<TelcoPromo>,
        @SerializedName("tickers")
        @Expose
        val tickers: List<TelcoTicker>
)