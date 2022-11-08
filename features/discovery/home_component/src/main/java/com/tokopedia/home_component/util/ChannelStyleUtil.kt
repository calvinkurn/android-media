package com.tokopedia.home_component.util

object ChannelStyleUtil {
    private const val KEY_BORDER_STYLE = "borderStyle"
    private const val KEY_DIVIDER_SIZE = "dividerSize"
    const val DEFAULT_DIVIDER_SIZE = 1
    const val BORDER_STYLE_BLEEDING = "bleeding"
    const val BORDER_STYLE_PADDING = "padding"

    fun String.parseDividerSize(): Int {
        val map = this.split("&").associate {
            val (key, value ) = it.split("=")
            key to value
        }
        val size = map[KEY_DIVIDER_SIZE]?.toIntOrNull()
        size?.let {
            return if(it > 0) it else DEFAULT_DIVIDER_SIZE
        }
        return DEFAULT_DIVIDER_SIZE
    }

    fun String.parseBorderStyle(): String {
        val map = this.split("&").associate {
            val (key, value ) = it.split("=")
            key to value
        }
        val size = map[KEY_BORDER_STYLE]
        size?.let {
            return if(it.isBlank()) it else BORDER_STYLE_BLEEDING
        }
        return BORDER_STYLE_BLEEDING
    }
}
