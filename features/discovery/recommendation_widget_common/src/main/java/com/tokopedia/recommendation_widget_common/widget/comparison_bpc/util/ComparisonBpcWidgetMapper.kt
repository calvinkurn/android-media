package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.util

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard.utils.getMaxHeightForListView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModels
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactory
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcItemModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs.BpcSpecsConfig
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs.BpcSpecsMapper
import com.tokopedia.recommendation_widget_common.widget.global.RecomWidgetTrackingModel
import kotlinx.coroutines.Dispatchers
import kotlin.math.abs

/**
 * Created by Frenzel
 */
object ComparisonBpcWidgetMapper {
    suspend fun mapToComparisonWidgetModel(
        recommendationWidget: RecommendationWidget,
        recommendationTrackingModel: RecomWidgetTrackingModel,
        context: Context
    ): List<Visitable<ComparisonBpcTypeFactory>> {
        val recommendationItems = recommendationWidget.recommendationItemList
        val specsConfig = buildSpecsConfig(recommendationItems, context)

        val productCardWidth = context.resources.getDimensionPixelSize(com.tokopedia.recommendation_widget_common.R.dimen.comparison_bpc_widget_product_card_width)
        val productCardHeight = recommendationWidget.recommendationItemList.toProductCardModels().getMaxHeightForListView(
            context,
            Dispatchers.IO
        )

        return recommendationItems.withIndex().map {
            val isEdgeStart = it.index == 0

            ComparisonBpcItemModel(
                specsModel = BpcSpecsMapper.mapToSpecsListModel(
                    it.value.specs, isEdgeStart, specsConfig, it.index, recommendationItems.size),
                productCardModel = it.value.toProductCardModel(),
                productCardHeight = productCardHeight,
                productCardWidth = productCardWidth,
                recommendationItem = it.value,
                recomWidgetTrackingModel = recommendationTrackingModel,
            )
        }
    }

    private fun buildSpecsConfig(recommendationItem: List<RecommendationItem>, context: Context): BpcSpecsConfig {
        val specsConfig = BpcSpecsConfig()
        val maxSpecsSize = recommendationItem.maxByOrNull{ it.specs.size }?.specs?.size?:0
        var comparisonWidth = context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_widget_product_card_width_measure)
        //substract with margin start and end
        comparisonWidth -= context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_margin_start)
        comparisonWidth -= context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_margin_end)
        for (i in 0 until maxSpecsSize) {
            val measuredSummaryHeight = BpcSpecsMapper.findMaxSummaryText(
                recommendationItem,
                i,
                comparisonWidth,
                context
            )

            val measuredTitleHeight = BpcSpecsMapper.findMaxTitleText(
                recommendationItem,
                i,
                comparisonWidth,
                context
            )

            var specsHeight = 0
            //add room for margin top (first item will be calculate translation Y as well)
            val firstSpecTranslationY = abs(context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_margin_start))
            val firstSpecExtraMarginTop = abs(context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_first_specs_extra_margin_top))
            val defaultTopPadding = context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_margin_top)
            val topPadding = if(i == 0) abs(firstSpecTranslationY) + firstSpecExtraMarginTop + defaultTopPadding else defaultTopPadding
            specsHeight += topPadding
            //add room for margin bottom
            specsHeight += context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_top)
            //add measured title height
            specsHeight += measuredTitleHeight
            //add measured summary height
            specsHeight += measuredSummaryHeight

            specsConfig.heightPositionMap[i] = specsHeight
        }

        return specsConfig
    }
}
