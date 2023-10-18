package com.tokopedia.recommendation_widget_common.widget.comparison

import android.content.Context
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsConfig
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsMapper
import kotlinx.coroutines.Dispatchers

object ComparisonWidgetMapper {
    suspend fun mapToComparisonWidgetModel(
        recommendationWidget: RecommendationWidget,
        context: Context,
        isAnchorClickable: Boolean,
        comparisonColorConfig: ComparisonColorConfig,
    ): ComparisonListModel {
        val recommendationItems = recommendationWidget.recommendationItemList
        val specsConfig = buildSpecsConfig(recommendationItems, context)
        val listOfProductCardModel = recommendationItems.map {
            it.toProductCardModel(forceLightMode = comparisonColorConfig.productCardForceLightMode)
        }
        val productCardHeight = listOfProductCardModel.getMaxHeightForGridView(
            context,
            Dispatchers.IO,
            context.resources.getDimensionPixelSize(R.dimen.comparison_widget_product_card_width)
        )
        val collapsedHeight = productCardHeight + getCollapsedSpecsHeight(specsConfig.heightPositionMap)
        return ComparisonListModel(
            recommendationWidget = recommendationWidget,
            comparisonData = recommendationItems.withIndex().map {
                val isEdgeStart = it.index == 0

                ComparisonModel(
                    specsModel = SpecsMapper.mapToSpecsListModel(
                        it.value.specs,
                        isEdgeStart,
                        specsConfig,
                        it.index,
                        recommendationItems.size,
                        comparisonColorConfig
                    ),
                    productCardModel = it.value.toProductCardModel(),
                    recommendationItem = it.value,
                    isClickable = isClickable(it.index, isAnchorClickable),
                )
            },
            comparisonWidgetConfig = ComparisonWidgetConfig(
                productCardHeight = productCardHeight,
                collapsedHeight = collapsedHeight,
            ),
            comparisonColorConfig = comparisonColorConfig,
        )
    }

    private fun isClickable(index: Int, isAnchorClickable: Boolean) =
        index != Int.ZERO || isAnchorClickable

    private fun getCollapsedSpecsHeight(heightPositionMap: MutableMap<Int, Int>): Int {
        //2 is number visible of specs height on collapsed state
        val maxCalculatedData = if (heightPositionMap.size >= 2) 2 else heightPositionMap.size
        var height = 0
        for (i in 0..maxCalculatedData) {
            height += heightPositionMap[i]?:0
        }
        return height
    }

    private fun buildSpecsConfig(recommendationItem: List<RecommendationItem>, context: Context): SpecsConfig {
        val specsConfig = SpecsConfig()
        val maxSpecsSize = recommendationItem.maxByOrNull{ it.specs.size }?.specs?.size?:0
        var comparisonWidth = context.resources.getDimensionPixelSize(R.dimen.comparison_widget_product_card_width_measure)
        //substract with margin start and end
        comparisonWidth -= context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_start)
        comparisonWidth -= context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_end)
        for (i in 0 until maxSpecsSize) {
            val measuredSummaryHeight =
                SpecsMapper.findMaxSummaryText(
                    recommendationItem,
                    i,
                    comparisonWidth,
                    context
                )

            //initial height for overall specs content, start from title height
            var specsHeight = context.resources.getDimensionPixelSize(R.dimen.comparison_title_text_height)
            //add room for margin top
            specsHeight += context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_top)
            //add room for margin bottom
            specsHeight += context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_top)
            //add measured summary height
            specsHeight += measuredSummaryHeight

            specsConfig.heightPositionMap[i] = specsHeight
        }

        return specsConfig
    }
}
