package com.tokopedia.home_component.util

import com.tokopedia.kotlin.extensions.view.ifNullOrBlank

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

    private const val KEY_PRODUCT_CARD_VERSION = "productCardVersion"
    private const val KEY_PRODUCT_CARD_VERSION_5 = "5.0"

    fun String.parseStyleParamAsMap(): Map<String, String> {
        return split("&").associate {
            val (key, value) = it.split("=")
            key to value
        }
    }

    fun Map<String, String>.parseDividerSize(): Int {
        try {
            val value = get(KEY_DIVIDER_SIZE)?.toIntOrNull()
            value?.let {
                return if (it > 0) it else DEFAULT_DIVIDER_SIZE
            }
        } catch (_: Exception) { }
        return DEFAULT_DIVIDER_SIZE
    }

    fun Map<String, String>.parseBorderStyle(): String {
        return get(KEY_BORDER_STYLE).ifNullOrBlank { BORDER_STYLE_BLEEDING }
    }

    fun Map<String, String>.parseImageStyle(): String {
        return get(KEY_IMAGE_STYLE,).ifNullOrBlank { IMAGE_STYLE_DEFAULT }
    }

    fun Map<String, String>.parseWithSubtitle(): Boolean {
        return try {
            get(KEY_WITH_SUBTITLE).toBoolean()
        } catch (_: Exception) {
            false
        }
    }

    fun Map<String, String>.isHideTimer(): Boolean {
        return try {
            get(KEY_HIDE_TIMER).toBoolean()
        } catch (_: Exception) {
            false
        }
    }

    fun Map<String, String>.isProductCardReimagine(): Boolean {
        return try {
            get(KEY_PRODUCT_CARD_VERSION) == KEY_PRODUCT_CARD_VERSION_5
        } catch (_: Exception) {
            false
        }
    }
}
