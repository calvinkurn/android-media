package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 03/06/21
 */

data class ShopScoreResponse(
        @SerializedName("shopScoreLevel")
        val shopScoreLevel: ShopScoreLevelModel? = null
)

data class ShopScoreLevelModel(
        @SerializedName("result")
        val result: ShopScoreResultModel? = null
)

data class ShopScoreResultModel(
        @SerializedName("shopScore")
        val shopScore: Double? = 0.0
)