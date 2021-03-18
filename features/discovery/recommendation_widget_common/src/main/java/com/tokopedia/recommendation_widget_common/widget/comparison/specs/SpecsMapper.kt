package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.content.Context
import android.text.Layout
import android.text.StaticLayout

import android.text.TextPaint
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationSpecificationLabels
import com.tokopedia.recommendation_widget_common.widget.comparison.ResponseMockData

object SpecsMapper {
    fun mapToSpecsListModel(
            recommendationSpecificationLabels: List<RecommendationSpecificationLabels>,
            parentInEdgeStart: Boolean = false,
            parentInEdgeEnd: Boolean = false,
            context: Context,
            specsConfig: SpecsConfig
    ): SpecsListModel {
        val listSpecsModel = recommendationSpecificationLabels.withIndex().map {
            SpecsModel(
                specsTitle = it.value.specTitle,
                specsSummary = it.value.specSummary,
                bgDrawableRef = getDrawableBasedOnParentPosition(
                    parentInEdgeStart,
                    parentInEdgeEnd
                ),
                bgDrawableColorRef = getColorBasedOnPosition(
                    it.index
                ),
                height = 400
            )
        }
        return SpecsListModel(
            specs = listSpecsModel,
            specsConfig = specsConfig
        )
    }

    private fun buildSpecsConfig(listSpecsModel: List<SpecsModel>, context: Context): SpecsConfig {
        val text =
            findMaxSummaryText(
                listSpecsModel
            )

        val textSizeHeight = context.resources.getDimensionPixelSize(R.dimen.comparison_summary_text_height)
        var comparisonWidth = context.resources.getDimensionPixelSize(R.dimen.comparison_specs_content_width)
        //substract with margin start and end
        comparisonWidth -= context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_start)
        comparisonWidth -= context.resources.getDimensionPixelSize(R.dimen.comparison_specs_margin_end)

        val measuredSummaryHeight =
            measureSummaryTextHeight(
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

        return SpecsConfig(
            heightPositionMap = mutableMapOf(Pair(0, specsHeight))
        )
    }

    private fun findMaxSummaryText(
        listSpecsModel: List<SpecsModel>
    ): String {
        var text = ""
        var maxSummaryChar = 0
        listSpecsModel.forEach {
            val summaryChar = it.specsSummary.count()
            if (maxSummaryChar < summaryChar) {
                maxSummaryChar = summaryChar
                text = it.specsSummary
            }
        }
        return text
    }

    fun getColorBasedOnPosition(position: Int): Int {
        return if (position % 2 == 0) {
            R.color.Unify_N50
        } else {
            R.color.Unify_N0
        }
    }

    fun getDrawableBasedOnParentPosition(
        parentInEdgeStart: Boolean,
        parentInEdgeEnd: Boolean
    ): Int {
        if (parentInEdgeStart) return R.drawable.bg_specs_start
        if (parentInEdgeEnd) return R.drawable.bg_specs_end
        return R.drawable.bg_specs
    }

    fun measureSummaryTextHeight(
        text: CharSequence?,
        textSize: Float,
        textWidth: Int
    ): Int {
        val myTextPaint = TextPaint()
        myTextPaint.isAntiAlias = true
        // this is how you would convert sp to pixels based on screen density
        //myTextPaint.setTextSize(16 * context.getResources().getDisplayMetrics().density);
        myTextPaint.textSize = textSize
        val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
        val spacingMultiplier = 1f
        val myStaticLayout = StaticLayout(
            text,
            myTextPaint,
            textWidth,
            alignment,
            spacingMultiplier,
            0f,
            true
        )
        return myStaticLayout.height
    }
}