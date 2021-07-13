package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.text.Layout
import android.text.StaticLayout

import android.text.TextPaint
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationSpecificationLabels
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.BODY_3
import com.tokopedia.unifyprinciples.getTypeface
import kotlinx.coroutines.*
import kotlin.math.roundToInt

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
        textSize: Float,
        textWidth: Int,
        context: Context
    ): Int {
//        val myTextPaint = TextPaint()
//        myTextPaint.isAntiAlias = true
//        // this is how you would convert sp to pixels based on screen density
//        myTextPaint.textSize = textSize
//        val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
//        val spacingMultiplier = 1.0f
//        myTextPaint.letterSpacing = 1.5f
        val params =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val paramsTextView =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
////        val textView = AppCompatTextView(context)
////        textView.text = text
////        textView.setLineSpacing(0.5f, 1f)
////        textView.layoutParams = params
////        textView.textSize = textSize
////        textView.minHeight = (textSize + 6.toPx()).toInt()
////        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(textWidth, View.MeasureSpec.AT_MOST)
////        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
////        textView.measure(0, 0)
////        textView.typeface = getTypeface(context, "RobotoRegular.ttf")
////        return (textView.paint.fontMetrics.bottom - textView.paint.fontMetrics.top).roundToInt()
//
//        val myStaticLayout = StaticLayout(
//            text,
//            myTextPaint,
//            textWidth,
//            alignment,
//            spacingMultiplier,
//            18f,
//            true
//        )
//
////        return myStaticLayout.height
//
////        return myTextPaint.getTextBounds()
        val myTextPaint = TextPaint()
        myTextPaint.isAntiAlias = true
        // this is how you would convert sp to pixels based on screen density
//        myTextPaint.setTextSize(textSize * context.getResources().getDisplayMetrics().density);
        myTextPaint.textSize = textSize

        val typography = Typography(context)
        typography.setType(BODY_3)
        typography.layoutParams = paramsTextView
        typography.text = MethodChecker.fromHtml(text.toString())
//        typography.textSize = textSize
        typography.measure(0,0)
        myTextPaint.textSize = typography.textSize
        myTextPaint.typeface = typography.typeface
        val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = params
        linearLayout.addView(typography)
        linearLayout.measure(0,0)
        val spacingMultiplier = 1.0f
        val myStaticLayout = StaticLayout(
            text ,
            typography.paint,
            textWidth,
            alignment,
            typography.lineSpacingMultiplier,
            typography.lineSpacingExtra,
            typography.includeFontPadding
        )
//        return (myStaticLayout.lineCount * typography.textSize * 1.0).toInt()
        var height = 0
        typography.post {
            height =typography.lineCount * typography.measuredHeight
        }
        myStaticLayout.lineCount* (typography.measuredHeight/typography.lineCount)
//        return linearLayout.measuredHeight


//        fun textHeight(text: CharSequence) {
//            val paramsTextView =
//                LinearLayout.LayoutParams(303, LinearLayout.LayoutParams.WRAP_CONTENT)
//            val typography = Typography(context)
//            typography.setType(BODY_3)
//            typography.layoutParams = paramsTextView
//            typography.text = text
//            typography.measure(0,0)
//            return typography.measuredHeight
//        }

        runBlocking {
            val measureHeight = async {
                typography.post{
                    height = typography.measuredHeight
                }
            }
            runBlocking {
                measureHeight.await()
            }
        }
        typography.post {
            height = typography.measuredHeight
        }.run {
            height = typography.measuredHeight
        }

//        val handler = Handler()
//        handler.post {
//            height = typography.measuredHeight
//        }

        return height
    }

    suspend fun textHeight(typography: Typography) : Int {
        var height = 0
        typography.post {
            height = typography.measuredHeight
        }
        return height
    }

    fun findMaxSummaryText(
        recommendationItem: List<RecommendationItem>,
        position: Int,
        textSize: Float,
        textWidth: Float,
        context: Context
    ) : Int {
        var maxHeight = 0
        for(i in recommendationItem)
        {
            val heightText = (measureSummaryTextHeight(
                i.specs[position].specSummary,
                textSize,
                textWidth.toInt(),
                context
            ))
            if(heightText > maxHeight) maxHeight = heightText
        }
        return maxHeight
    }
}