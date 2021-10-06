package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by yfsx on 06/10/21.
 */
interface RecommendationCarouselWidgetBindPageNameListener : RecomCarouselWidgetBasicListener {

    //leave this empty if your widget doesn't implement tokonow
    fun onMiniCartUpdatedFromRecomWidget(miniCartSimplifiedData: MiniCartSimplifiedData)

    //leave this empty if your widget doesn't implement tokonow
    fun onRecomTokonowAtcSuccess(message: String)

    //leave this empty if your widget doesn't implement tokonow
    fun onRecomTokonowAtcFailed(throwable: Throwable)

    //leave this empty if your widget doesn't implement tokonow
    fun onRecomTokonowAtcNeedToSendTracker(recommendationItem: RecommendationItem)

    //leave this empty if your widget doesn't implement tokonow
    fun onRecomTokonowDeleteNeedToSendTracker(recommendationItem: RecommendationItem)

    fun onClickItemNonLoginState()

}