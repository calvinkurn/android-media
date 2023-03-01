package com.tokopedia.home_component.util

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull

/**
 * Created by frenzel
 */
object TodoWidgetUtil {
    private const val FE_PARAM_KEY_CLOSE = "close"
    private const val TODO_WIDGET_OUTER_RIGHT_PADDING = 14f
    private const val TODO_WIDGET_INSIDE_LEFT_RIGHT_PADDING = 12f
    private const val TODO_WIDGET_IMAGE_TO_CONTENT_PADDING = 12f
    private const val TODO_WIDGET_CONTENT_TO_CTA_PADDING = 8f

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

    fun measureTodoWidgetCardMaxWidth(
        context: Context
    ): Int {
        val screenWidth = DeviceScreenInfo.getScreenWidth(context)
        return screenWidth - (2 * TODO_WIDGET_OUTER_RIGHT_PADDING.toDpInt())
    }

    fun measureTodoWidgetContentMaxWidth(
        context: Context
    ): Int {
        val cardMaxWidth = measureTodoWidgetCardMaxWidth(context)
        val paddings = (2 * TODO_WIDGET_INSIDE_LEFT_RIGHT_PADDING) + TODO_WIDGET_IMAGE_TO_CONTENT_PADDING + TODO_WIDGET_CONTENT_TO_CTA_PADDING
        return cardMaxWidth - paddings.toDpInt() - context.resources.getDimensionPixelSize(R.dimen.home_todo_widget_cta_width) - context.resources.getDimensionPixelSize(R.dimen.home_todo_widget_image_size)
    }
}
