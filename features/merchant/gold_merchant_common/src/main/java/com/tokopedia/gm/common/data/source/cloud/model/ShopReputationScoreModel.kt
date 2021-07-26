package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 23/03/21
 */

data class ShopReputationScoreModel(
        @SerializedName("score")
        val score: String
)