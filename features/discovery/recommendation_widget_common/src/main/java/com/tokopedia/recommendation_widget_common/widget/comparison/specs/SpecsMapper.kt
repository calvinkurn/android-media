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

    fun measureSummaryTextHeight(
        text: CharSequence?,
        textSize: Float,
        textWidth: Int,
        context: Context
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
}