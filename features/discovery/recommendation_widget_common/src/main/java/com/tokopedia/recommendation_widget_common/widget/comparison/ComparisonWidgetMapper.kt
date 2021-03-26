package com.tokopedia.recommendation_widget_common.widget.comparison

import android.content.Context
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
        context: Context
    ): ComparisonListModel {
        val recommendationItems = recommendationWidget.recommendationItemList
        val specsConfig = buildSpecsConfig(recommendationItems, context)
        val listOfProductCardModel = recommendationItems.map {
            it.toProductCardModel()
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
                val isEdgeEnd = it.index == (recommendationItems.size - 1)

                ComparisonModel(
                    specsModel = SpecsMapper.mapToSpecsListModel(
                            it.value.specs, isEdgeStart, isEdgeEnd, context, specsConfig),
                        productCardModel = it.value.toProductCardModel(),
                        recommendationItem = it.value,
                        isCurrentItem = checkIseCurrentItem(it.index)
                )
            },
                comparisonWidgetConfig = ComparisonWidgetConfig(
                        productCardHeight = productCardHeight,
                        collapsedHeight = collapsedHeight
                )
        )
    }

    private fun checkIseCurrentItem(index: Int): Boolean {
        return index == 0
    }

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
        var specsConfig = SpecsConfig()
        val maxSpecsSize = recommendationItem.maxBy { it.specs.size }?.specs?.size?:0
        for (i in 0 until maxSpecsSize) {
            var currentPosition = i
            val text = findMaxSummaryText(recommendationItem, i)
            val textSizeHeight = context.resources.getDimensionPixelSize(R.dimen.comparison_summary_text_height)
            var comparisonWidth = context.resources.getDimensionPixelSize(R.dimen.comparison_specs_calculation_width)
            //substract with margin start and end
            comparisonWidth -= context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_start)
            comparisonWidth -= context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_end)

            val measuredSummaryHeight =
                SpecsMapper.measureSummaryTextHeight(
                    text,
                    textSizeHeight.toFloat(),
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

            specsConfig.heightPositionMap.put(currentPosition, specsHeight)
        }

        return specsConfig
    }

    private fun findMaxSummaryText(
        listResponseData: List<RecommendationItem>,
        summaryPosition: Int
    ): String {
        var text = ""
        var maxSummaryChar = 0
        listResponseData.forEach {
            val summary = it.specs[summaryPosition].specSummary
            val summaryCharCount = summary.count()
            if (maxSummaryChar < summaryCharCount) {
                maxSummaryChar = summaryCharCount
                text = summary
            }
        }
        return text
    }
}