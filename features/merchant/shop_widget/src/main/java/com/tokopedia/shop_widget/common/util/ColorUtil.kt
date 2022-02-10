package com.tokopedia.shop_widget.common.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat

object ColorUtil {

    private const val FORMAT_STRING_COLOR = "#%06x"
    private const val FORMAT_HEX_COLOR = 0xffffff
    private const val FORMAT_PREFIX_HEX_COLOR = "#"

    fun getBackGroundColor(context: Context, color: String?, colorRes: Int): Int {
        return try {
            Color.parseColor(getStringColor(context, color, colorRes))
        } catch (e: Exception) {
            ContextCompat.getColor(context, colorRes)
        }
    }

    private fun getStringColor(context: Context, color: String?, colorRes: Int): String {
        return if (color.isNullOrEmpty()) {
            String.format(FORMAT_STRING_COLOR, ContextCompat.getColor(context, colorRes) and FORMAT_HEX_COLOR)
        } else {
            if (!color.startsWith(FORMAT_PREFIX_HEX_COLOR)) {
                FORMAT_PREFIX_HEX_COLOR + color
            } else {
                color
            }
        }
    }
}