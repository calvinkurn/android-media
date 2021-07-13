package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.content.Context

import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationSpecificationLabels
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.BODY_3

object SpecsMapper {
    fun mapToSpecsListModel(
        recommendationSpecificationLabels: List<RecommendationSpecificationLabels>,
        parentInEdgeStart: Boolean = false,
        specsConfig: SpecsConfig,
        position: Int,
        totalItems: Int
    ): SpecsListModel {
        val listSpecsModel = recommendationSpecificationLabels.withIndex().map {
            SpecsModel(
                specsTitle = if (position == 0) it.value.specTitle else "",
                specsSummary = it.value.specSummary,
                bgDrawableRef = getDrawableBasedOnParentCompareItemPosition(
                    it.index,
                    recommendationSpecificationLabels.size
                ),
                bgDrawableColorRef = getColorCompareItem(
                    parentInEdgeStart
                )
            )
        }
        return SpecsListModel(
            specs = listSpecsModel,
            specsConfig = specsConfig,
            currentRecommendationPosition = position,
            totalRecommendations = totalItems
        )
    }

    private fun getDrawableBasedOnParentCompareItemPosition(
        index: Int,
        size: Int
    ): Int {
        if (index == 0) return R.drawable.bg_specs_start_end_top
        else if (index != size-1) return R.drawable.bg_specs
        return R.drawable.bg_specs_start_end_bottom
    }

    private fun getColorCompareItem(parentInEdgeStart: Boolean): Int {
        return if (parentInEdgeStart) {
            com.tokopedia.unifyprinciples.R.color.Unify_N50
        } else {
            com.tokopedia.unifyprinciples.R.color.Unify_N0
        }
    }

    private fun measureSummaryTextHeight(
        text: CharSequence?,
        textWidth: Int,
        context: Context
    ): Int {
        val params =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val paramsTextView =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val typography = Typography(context)
        typography.setType(BODY_3)
        typography.layoutParams = paramsTextView
        typography.text = MethodChecker.fromHtml(text.toString())
        typography.measure(0,0)
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = params
        linearLayout.addView(typography)
        linearLayout.measure(0,0)
        typography.post {}.run {
            return typography.measuredHeight
        }
    }

    fun findMaxSummaryText(
        recommendationItem: List<RecommendationItem>,
        position: Int,
        textWidth: Int,
        context: Context
    ) : Int {
        var maxHeight = 0
        for(i in recommendationItem)
        {
            val heightText = (measureSummaryTextHeight(
                i.specs[position].specSummary,
                textWidth,
                context
            ))
            if(heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }
}