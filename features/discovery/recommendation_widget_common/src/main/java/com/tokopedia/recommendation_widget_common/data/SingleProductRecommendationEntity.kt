package com.tokopedia.recommendation_widget_common.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 29/08/19
 */

@SuppressLint("Invalid Data Type")
data class SingleProductRecommendationEntity (
    @SerializedName("productRecommendationWidgetSingle")
    val productRecommendationWidget: ProductRecommendationWidgetSingle
){
    data class ProductRecommendationWidgetSingle(@SerializedName("data") val data: RecommendationEntity.RecommendationData = RecommendationEntity.RecommendationData())

}
