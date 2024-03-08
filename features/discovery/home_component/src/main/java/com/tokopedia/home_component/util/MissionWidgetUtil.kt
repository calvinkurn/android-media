package com.tokopedia.home_component.util

import android.content.Context
import android.widget.LinearLayout
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by frenzel
 */
abstract class MissionWidgetUtil {
    abstract fun getWidth(context: Context): Int

    abstract fun findMaxTitleHeight(
        data: MissionWidgetListDataModel,
        width: Int,
        context: Context
    ): Int

    abstract fun findMaxSubtitleHeight(
        data: MissionWidgetListDataModel,
        width: Int,
        context: Context
    ): Int

    fun measureTextHeight(
        context: Context,
        text: CharSequence?,
        textWidth: Int,
        typographyType: Int,
        typographyWeight: Int,
        maxLines: Int,
        hideWhenEmpty: Boolean = false,
    ): Int {
        if(hideWhenEmpty && text.isNullOrEmpty()) return 0
        val params =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val paramsTextView =
            LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        val typography = Typography(context)
        typography.setType(typographyType)
        typography.setWeight(typographyWeight)
        typography.layoutParams = paramsTextView
        typography.text = text
        typography.maxLines = maxLines
        typography.measure(DEFAULT_LAYOUT_SPEC, DEFAULT_LAYOUT_SPEC)
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = params
        linearLayout.addView(typography)
        linearLayout.measure(DEFAULT_LAYOUT_SPEC, DEFAULT_LAYOUT_SPEC)
        typography.post {}.run {
            return typography.measuredHeight
        }
    }

    companion object {
        private const val DEFAULT_LAYOUT_SPEC = 0
    }
}
