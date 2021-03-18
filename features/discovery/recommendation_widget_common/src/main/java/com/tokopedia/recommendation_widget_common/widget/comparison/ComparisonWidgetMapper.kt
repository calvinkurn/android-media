package com.tokopedia.recommendation_widget_common.widget.comparison

import android.content.Context
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsConfig
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsMapper

object ComparisonWidgetMapper {
    fun mapToComparisonWidgetModel(
        responseMockDatas: List<ResponseMockData>,
        context: Context
    ): ComparisonListModel {
        val productCardHeight = 500
        val specsConfig = buildSpecsConfig(responseMockDatas, context)
        val collapsedHeight = productCardHeight + getCollapsedSpecsHeight(specsConfig.heightPositionMap)
        return ComparisonListModel(
            comparisonData = responseMockDatas.withIndex().map {
                val isEdgeStart = it.index == 0
                val isEdgeEnd = it.index == (responseMockDatas.size - 1)

                ComparisonModel(
                    SpecsMapper.mapToSpecsListModel(
                        it.value, isEdgeStart, isEdgeEnd, context, specsConfig
                    )
                )
            },
                comparisonWidgetConfig = ComparisonWidgetConfig(
                        productCardHeight = productCardHeight,
                        collapsedHeight = collapsedHeight
                )
        )
    }

    private fun getCollapsedSpecsHeight(heightPositionMap: MutableMap<Int, Int>): Int {
        val maxCalculatedData = if (heightPositionMap.size >= 3) 3 else heightPositionMap.size
        var height = 0
        for (i in 0..maxCalculatedData) {
            height += heightPositionMap[i]?:0
        }
        return height
    }

    private fun buildSpecsConfig(listResponseData: List<ResponseMockData>, context: Context): SpecsConfig {
        var specsConfig = SpecsConfig()
        val maxSpecsSize = listResponseData.maxBy { it.list.size }?.list?.size?:0
        for (i in 0 until maxSpecsSize) {
            var currentPosition = i
            val text =
                findMaxSummaryText(
                    listResponseData,
                    i
                )

            val textSizeHeight = context.resources.getDimensionPixelSize(R.dimen.comparison_summary_text_height)
            var comparisonWidth = context.resources.getDimensionPixelSize(R.dimen.comparison_specs_content_width)
            //substract with margin start and end
            comparisonWidth -= context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_start)
            comparisonWidth -= context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_end)

            val measuredSummaryHeight =
                SpecsMapper.measureSummaryTextHeight(
                    text,
                    textSizeHeight.toFloat(),
                    comparisonWidth
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
        listResponseData: List<ResponseMockData>,
        summaryPosition: Int
    ): String {
        var text = ""
        var maxSummaryChar = 0
        listResponseData.forEach {
            val summary = it.list[summaryPosition].second
            val summaryCharCount = summary.count()
            if (maxSummaryChar < summaryCharCount) {
                maxSummaryChar = summaryCharCount
                text = summary
            }
        }
        return text
    }
}