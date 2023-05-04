package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs

import android.content.Context

import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationSpecificationLabels
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.BOLD
import com.tokopedia.unifyprinciples.Typography.Companion.DISPLAY_3

/**
 * Created by Frenzel
 */
object BpcSpecsMapper {
    fun mapToSpecsListModel(
        recommendationSpecificationLabels: List<RecommendationSpecificationLabels>,
        parentInEdgeStart: Boolean = false,
        specsConfig: BpcSpecsConfig,
        position: Int,
        totalItems: Int
    ): BpcSpecsListModel {
        val listSpecsModel = recommendationSpecificationLabels.withIndex().map {
            BpcSpecsModel(
                specsTitle = it.value.specTitle,
                specsSummary = it.value.specSummary,
                specsSummaryBullet = it.value.recommendationSpecificationLabelsBullet.map { content ->
                    BpcSpecsSummaryBullet(
                        specsSummary = content.specsSummary,
                        icon = content.icon
                    )
                },
                bgDrawableRef = getDrawableBasedOnParentCompareItemPosition(
                    it.index,
                    recommendationSpecificationLabels.size
                ),
                bgDrawableColorRef = getColorCompareItem(
                    parentInEdgeStart
                )
            )
        }
        return BpcSpecsListModel(
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
        if (index == 0) return R.drawable.bg_specs
        else if (index != size - 1) return R.drawable.bg_specs
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
        summary: RecommendationSpecificationLabels,
        textWidth: Int,
        context: Context
    ): Int {
        return if(summary.specSummary.isNotEmpty()) {
            measureEachSummaryHeight(summary.specSummary, textWidth, context)
        } else if(summary.recommendationSpecificationLabelsBullet.isNotEmpty()) {
            var totalHeight = 0
            for(i in summary.recommendationSpecificationLabelsBullet) {
                val heightText = (measureEachSummaryBulletHeight(
                    i.specsSummary,
                    !i.icon.isNullOrBlank(),
                    textWidth,
                    context
                ))
                totalHeight += heightText
            }
            return totalHeight
        } else 0
    }

    private fun measureEachSummaryHeight(
        text: CharSequence?,
        textWidth: Int,
        context: Context
    ): Int {
        val params =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val paramsTextView =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val typography = Typography(context)
        typography.setType(DISPLAY_3)
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

    private fun measureEachSummaryBulletHeight(
        text: CharSequence?,
        useIcon: Boolean,
        containerWidth: Int,
        context: Context
    ): Int {
        val iconSize = if(useIcon) context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_icon_size)
        else context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_bullet_size)
        val textWidth = if(useIcon) {
            containerWidth - context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_icon_margin) - iconSize
        } else containerWidth - (2 * context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_bullet_margin)) - iconSize
        val params =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val paramsTextView =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val typography = Typography(context)
        typography.setType(DISPLAY_3)
        typography.layoutParams = paramsTextView
        typography.text = MethodChecker.fromHtml(text.toString())
        typography.measure(0,0)
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = params
        linearLayout.addView(typography)
        linearLayout.measure(0,0)
        typography.post {}.run {
            return typography.measuredHeight.coerceAtLeast(iconSize)
        }
    }

    fun findMaxSummaryText(
        recommendationItem: List<RecommendationItem>,
        position: Int,
        textWidth: Int,
        context: Context
    ) : Int {
        var maxHeight = 0
        for(i in recommendationItem) {
            val heightText = (measureSummaryTextHeight(
                i.specs[position],
                textWidth,
                context
            ))
            if(heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }

    private fun measureTitleTextHeight(
        text: CharSequence?,
        textWidth: Int,
        context: Context
    ): Int {
        val params =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val paramsTextView =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val typography = Typography(context)
        typography.setType(DISPLAY_3)
        typography.setWeight(BOLD)
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

    fun findMaxTitleText(
        recommendationItem: List<RecommendationItem>,
        position: Int,
        textWidth: Int,
        context: Context
    ) : Int {
        var maxHeight = 0
        for(i in recommendationItem)
        {
            val heightText = (measureTitleTextHeight(
                i.specs[position].specTitle,
                textWidth,
                context
            ))
            if(heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }
}
