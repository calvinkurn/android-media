package com.tokopedia.home_component.util

import android.content.Context
import android.widget.LinearLayout
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.TodoWidgetDataModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by frenzel
 */
object TodoWidgetUtil {
    private const val FE_PARAM_KEY_CLOSE = "close"
    private const val MAX_LINES_DUE_DATE = 1
    private const val MAX_LINES_DESCRIPTION = 2
    private const val DEFAULT_LAYOUT_SPEC = 0

    fun String.parseCloseParam(): String {
        return try {
            val map = this.split("&").associate {
                val (key, value) = it.split("=")
                key to value
            }
            map[FE_PARAM_KEY_CLOSE].toEmptyStringIfNull()
        } catch (e: Exception) {
            ""
        }
    }

    private fun measureTodoWidgetHeight(
        text: CharSequence?,
        context: Context
    ): Int {
        val params =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val paramsTextView =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val typography = Typography(context)
        typography.setType(Typography.DISPLAY_3)
        typography.setWeight(Typography.BOLD)
        typography.layoutParams = paramsTextView
        typography.text = text
        typography.maxLines = MAX_LINES_DUE_DATE
        typography.measure(DEFAULT_LAYOUT_SPEC, DEFAULT_LAYOUT_SPEC)
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = params
        linearLayout.addView(typography)
        linearLayout.measure(DEFAULT_LAYOUT_SPEC, DEFAULT_LAYOUT_SPEC)

        typography.post {}.run {
            return typography.measuredHeight
        }
    }

    fun findMaxTodoWidgetHeight(
        todoWidgetList: List<TodoWidgetDataModel>,
        context: Context
    ): Int {
        var maxHeight = 0
        var subtitleWidth = context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_image_size)

        // substract with margin start and end
        subtitleWidth -= context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_margin_horizontal_subtitle)
        subtitleWidth -= context.resources.getDimensionPixelSize(R.dimen.home_mission_widget_margin_horizontal_subtitle)
        for (todoWidget in todoWidgetList) {
            val heightText = TodoWidgetUtil.measureTodoWidgetHeight(
                todoWidget.contextInfo,
                context
            )
            if (heightText > maxHeight) maxHeight = heightText
        }

        return maxHeight
    }
}
