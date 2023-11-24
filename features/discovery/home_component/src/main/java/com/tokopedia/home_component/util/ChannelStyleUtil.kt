package com.tokopedia.home_component.util

object ChannelStyleUtil {
    private const val KEY_DIVIDER_SIZE = "dividerSize"
    const val DEFAULT_DIVIDER_SIZE = 1

    private const val KEY_BORDER_STYLE = "borderStyle"
    const val BORDER_STYLE_BLEEDING = "bleeding"
    const val BORDER_STYLE_PADDING = "padding"

    private const val KEY_IMAGE_STYLE = "imageStyle"
    const val IMAGE_STYLE_FULL = "full"
    const val IMAGE_STYLE_DEFAULT = ""

    private const val KEY_WITH_SUBTITLE = "withSubtitle"

    private const val KEY_HIDE_TIMER = "hideTimer"

    private fun String.parseStyleParamAsMap(): Map<String, String> {
        return split("&").associate {
            val (key, value) = it.split("=")
            key to value
        }
    }

    fun String.parseDividerSize(): Int {
        try {
            val map = this.parseStyleParamAsMap()
            val value = map[KEY_DIVIDER_SIZE]?.toIntOrNull()
            value?.let {
                return if (it > 0) it else DEFAULT_DIVIDER_SIZE
            }
        } catch (_: Exception) {
            return DEFAULT_DIVIDER_SIZE
        }
        return DEFAULT_DIVIDER_SIZE
    }

    fun String.parseBorderStyle(): String {
        try {
            val map = this.parseStyleParamAsMap()
            val value = map[KEY_BORDER_STYLE]
            value?.let {
                return it.ifBlank { BORDER_STYLE_BLEEDING }
            }
        } catch (_: Exception) {
            return BORDER_STYLE_BLEEDING
        }
        return BORDER_STYLE_BLEEDING
    }

    fun String.parseImageStyle(): String {
        try {
            val map = this.parseStyleParamAsMap()
            val value = map[KEY_IMAGE_STYLE]
            value?.let {
                return it.ifBlank { IMAGE_STYLE_DEFAULT }
            }
        } catch (_: Exception) {
            return IMAGE_STYLE_DEFAULT
        }
        return IMAGE_STYLE_DEFAULT
    }

    fun String.parseWithSubtitle(): Boolean {
        return try {
            val map = this.parseStyleParamAsMap()
            map[KEY_WITH_SUBTITLE].toBoolean()
        } catch (_: Exception) {
            false
        }
    }

    fun String.isHideTimer(): Boolean {
        return try {
            val map = this.parseStyleParamAsMap()
            map[KEY_HIDE_TIMER].toBoolean()
        } catch (_: Exception) {
            false
        }
    }
}
