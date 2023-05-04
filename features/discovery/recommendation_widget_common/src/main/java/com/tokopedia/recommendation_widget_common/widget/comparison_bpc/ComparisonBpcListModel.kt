package com.tokopedia.recommendation_widget_common.widget.comparison_bpc

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

data class ComparisonBpcListModel(
    val listData: List<Visitable<ComparisonBpcTypeFactory>> = listOf(),
    val trackingModel: RecommendationWidgetTrackingModel = RecommendationWidgetTrackingModel(),
    val itemsToShow: Int = DEFAULT_ITEM_TO_SHOW,
){
    companion object {
        const val DEFAULT_ITEM_TO_SHOW = 6
    }

    fun isShowViewAllCard() = itemsToShow < listData.size
}
