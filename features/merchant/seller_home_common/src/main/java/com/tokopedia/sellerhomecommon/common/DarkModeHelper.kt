package com.tokopedia.sellerhomecommon.common

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifyprinciples.stringToUnifyColor
import com.tokopedia.utils.resources.isAppDarkMode
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 12/05/23.
 */

class DarkModeHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val HEX_COLOR_REGEX = "#([a-fA-F0-9]{8}|[a-fA-F0-9]{6}|[a-fA-F0-9]{3})"
        private const val HEX_COLOR_FORMAT = "#%06X"
    }

    fun getUnifyHexColor(hexColor: String): String {
        try {
            val colorResId = stringToUnifyColor(context, hexColor).unifyColorResourceID.orZero()
            if (colorResId != Int.ZERO) {
                val color = context.getResColor(colorResId)
                return String.format(HEX_COLOR_FORMAT, 0xFFFFFF and color)
            }
            return hexColor
        } catch (ignore: IllegalArgumentException) {
            return hexColor
        }
    }

    fun makeHtmlDarkModeSupport(text: String): String {
        if (text.isBlank() || !isAppDarkMode()) {
            return text
        }

        val pattern = Pattern.compile(HEX_COLOR_REGEX)
        val matcher = pattern.matcher(text)
        var newText = text

        while (matcher.find()) {
            val hexColor = matcher.group()
            val unifyHexColor = getUnifyHexColor(hexColor)
            newText = newText.replace(hexColor, unifyHexColor)
        }

        return newText
    }
}