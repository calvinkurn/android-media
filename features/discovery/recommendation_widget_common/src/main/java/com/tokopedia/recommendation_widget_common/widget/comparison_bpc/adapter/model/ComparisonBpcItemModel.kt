package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactory
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs.BpcSpecsListModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

/**
 * Created by Frenzel
 */
data class ComparisonBpcItemModel(
    val specsModel: BpcSpecsListModel,
    val productCardModel: ProductCardModel,
    val productCardHeight: Int,
    val productCardWidth: Int,
    val recommendationItem: RecommendationItem,
    val anchorProductId: String,
    val trackingModel: RecommendationWidgetTrackingModel,
    val widgetTitle: String,
    val appLogAdditionalParam: AppLogAdditionalParam,
) : Visitable<ComparisonBpcTypeFactory> {
    override fun type(typeFactory: ComparisonBpcTypeFactory): Int {
        return typeFactory.type(this)
    }
}
