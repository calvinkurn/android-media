package com.tokopedia.common_digital.common.presentation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 13/11/18.
 */
abstract class RecommendationEntity {

    @SerializedName("rechargeFavoriteRecommendationList")
    @Expose
    val rechargeFavoriteRecommendationList: RechargeFavoriteRecommendationList = RechargeFavoriteRecommendationList()
}
