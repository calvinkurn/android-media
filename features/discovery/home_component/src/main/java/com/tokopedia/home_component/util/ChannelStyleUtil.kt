package com.tokopedia.home_component.util

object ChannelStyleUtil {
    private const val KEY_BORDER_STYLE = "borderStyle"
    private const val KEY_IMAGE_STYLE = "imageStyle"
    private const val KEY_DIVIDER_SIZE = "dividerSize"
    const val DEFAULT_DIVIDER_SIZE = 1
    const val BORDER_STYLE_BLEEDING = "bleeding"
    const val BORDER_STYLE_PADDING = "padding"
    const val IMAGE_STYLE_FULL = "full"
    const val IMAGE_STYLE_DEFAULT = ""

    fun String.parseDividerSize(): Int {
        try {
            val map = this.split("&").associate {
                val (key, value) = it.split("=")
                key to value
            }
            val size = map[KEY_DIVIDER_SIZE]?.toIntOrNull()
            size?.let {
                return if (it > 0) it else DEFAULT_DIVIDER_SIZE
            }
        } catch (e: Exception) {
            return DEFAULT_DIVIDER_SIZE
        }
        return DEFAULT_DIVIDER_SIZE
    }

    fun String.parseBorderStyle(): String {
        try {
            val map = this.split("&").associate {
                val (key, value) = it.split("=")
                key to value
            }
            val size = map[KEY_BORDER_STYLE]
            size?.let {
                return it.ifBlank { BORDER_STYLE_BLEEDING }
            }
        } catch (e: Exception) {
            return BORDER_STYLE_BLEEDING
        }
        return BORDER_STYLE_BLEEDING
    }

    fun String.parseImageStyle(): String {
        try {
            val map = this.split("&").associate {
                val (key, value) = it.split("=")
                key to value
            }
            val size = map[KEY_IMAGE_STYLE]
            size?.let {
                return it.ifBlank { IMAGE_STYLE_DEFAULT }
            }
        } catch (e: Exception) {
            return IMAGE_STYLE_DEFAULT
        }
        return IMAGE_STYLE_DEFAULT
    }
}
