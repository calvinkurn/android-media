package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactory
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs.BpcSpecsListModel
import com.tokopedia.recommendation_widget_common.widget.global.RecomWidgetTrackingModel

/**
 * Created by Frenzel
 */
data class ComparisonBpcItemModel(
    val specsModel: BpcSpecsListModel,
    val productCardModel: ProductCardModel,
    val productCardHeight: Int,
    val productCardWidth: Int,
    val recommendationItem: RecommendationItem,
    val recomWidgetTrackingModel: RecomWidgetTrackingModel,
): Visitable<ComparisonBpcTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ComparisonBpcTypeFactory): Int {
        return typeFactory.type(this)
    }
}
