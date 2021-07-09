package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.text.Layout
import android.text.StaticLayout

import android.text.TextPaint
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationSpecificationLabels

object SpecsMapper {
    fun mapToSpecsListModel(
            recommendationSpecificationLabels: List<RecommendationSpecificationLabels>,
            parentInEdgeStart: Boolean = false,
            specsConfig: SpecsConfig,
            position: Int
    ): SpecsListModel {
        val listSpecsModel = recommendationSpecificationLabels.withIndex().map {
            SpecsModel(
                specsTitle = if(position == 0) it.value.specTitle else "",
                specsSummary = it.value.specSummary,
                bgDrawableRef = getDrawableBasedOnParentCompareItemPosition(it.index, recommendationSpecificationLabels.size),
                bgDrawableColorRef = getColorCompareItem(parentInEdgeStart
                ),
                height = 400
            )
        }
        return SpecsListModel(
            specs = listSpecsModel,
            specsConfig = specsConfig
        )
    }

    fun mapToSpecsListCompareItemModel(
        recommendationSpecificationLabels: List<RecommendationSpecificationLabels>,
        specsConfig: SpecsConfig,
        parentInEdgeStart: Boolean = false,
        position: Int
    ): SpecsListModel {
        val listSpecsModel = recommendationSpecificationLabels.withIndex().map {
            SpecsModel(
                specsTitle = if(position == 0) it.value.specTitle else "",
                specsSummary = it.value.specSummary,
                bgDrawableRef = getDrawableBasedOnParentCompareItemPosition(it.index, recommendationSpecificationLabels.size),
                bgDrawableColorRef = getColorCompareItem(parentInEdgeStart
                ),
                height = 400
            )
        }
        return SpecsListModel(
            specs = listSpecsModel,
            specsConfig = specsConfig
        )
    }

    fun getColorBasedOnPosition(position: Int): Int {
        return if (position % 2 == 0) {
            com.tokopedia.unifyprinciples.R.color.Unify_N50
        } else {
            com.tokopedia.unifyprinciples.R.color.Unify_N0
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
        textSize: Float,
        textWidth: Int
    ): Int {
        val myTextPaint = TextPaint()
        myTextPaint.isAntiAlias = true
        // this is how you would convert sp to pixels based on screen density
//        myTextPaint.setTextSize(textSize * context.getResources().getDisplayMetrics().density);
        myTextPaint.textSize = textSize
        val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
        val spacingMultiplier = 1.0f
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

    fun findMaxSummaryText(
        recommendationItem: List<RecommendationItem>,
        position: Int,
        textSize: Float,
        textWidth: Int
    ) : Int {
        var maxHeight = 0
        for(i in recommendationItem)
        {
            val heightText = (measureSummaryTextHeight(i.specs[position].specSummary, textSize, textWidth))
            if(heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }
}