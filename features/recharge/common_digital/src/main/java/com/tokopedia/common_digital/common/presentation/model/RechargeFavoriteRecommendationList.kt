package com.tokopedia.common_digital.common.presentation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 14/11/18.
 */
class RechargeFavoriteRecommendationList {

    @SerializedName("title")
    @Expose
    val title: String = ""

    @SerializedName("recommendations")
    @Expose
    val recommendationItemEntityList: List<RecommendationItemEntity> = listOf()
}
