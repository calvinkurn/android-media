package com.tokopedia.recommendation_widget_common.widget.productcard.common

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

/**
 * @author by yoasfs on 09/06/20
 */

//listener for grid product card
interface RecomCommonProductCardListener {

    //product card click and impression
    fun onProductCardImpressed(data: RecommendationWidget, recomItem: RecommendationItem, position: Int)
    fun onProductCardClicked(data: RecommendationWidget, recomItem: RecommendationItem, position: Int, applink: String)

    //for see more card click
    fun onSeeMoreCardClicked(data: RecommendationWidget, applink: String)

    //for banner card clicked
    fun onBannerCardClicked(data: RecommendationWidget, applink: String)
    fun onBannerCardImpressed(data: RecommendationWidget, applink: String)
}