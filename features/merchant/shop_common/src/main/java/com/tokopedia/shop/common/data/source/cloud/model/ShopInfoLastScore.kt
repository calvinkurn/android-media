package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoLastScore {
    @SerializedName("count_score_bad")
    @Expose
    var countScoreBad: String? = null

    @SerializedName("count_score_good")
    @Expose
    var countScoreGood: String? = null

    @SerializedName("count_score_neutral")
    @Expose
    var countScoreNeutral: String? = null
}