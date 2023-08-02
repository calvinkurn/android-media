package com.tokopedia.utils.view

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.tokopedia.unifyprinciples.stringToUnifyColor
import java.util.regex.Pattern

/**
 * Created by Yehezkiel on 20/11/20
 */
object DarkModeUtil {

    const val HEX_COLOR_REGEX = "#([a-fA-F0-9]{8}|[a-fA-F0-9]{6}|[a-fA-F0-9]{3})"
    private const val HEX_COLOR_FORMAT = "#%06X"

    @JvmStatic
    fun Context?.isDarkMode(): Boolean {
        if (this == null) return false
        val mode = this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

    fun getHtmlTextDarkModeSupport(context: Context, text: String): String {
        if (text.isBlank() || !isDarkModeApp()) {
            return text
        }

        val pattern = Pattern.compile(HEX_COLOR_REGEX)
        val matcher = pattern.matcher(text)
        var newText = text

        while (matcher.find()) {
            val hexColor = matcher.group()
            val unifyHexColor = getUnifyHexColor(context, hexColor)
            newText = newText.replace(hexColor, unifyHexColor)
        }

        return newText
    }

    fun getUnifyHexColor(context: Context, hexColor: String): String {
        runCatching {
            val unify = stringToUnifyColor(context, hexColor)
            val unifyColor = unify.unifyColor
            if (unifyColor != null) {
                return getHexColorByColorInt(unifyColor)
            }
        }
        return hexColor
    }

    /**
     * The ContextCompat.getColor() method retrieves the color value as an integer.
     * We then convert this integer to a hex color string using String.format() and
     * the "%06X" format specifier.
     *
     * In the context of the expression (0xFFFFFF & colorValue), 0xFFFFFF is
     * a hexadecimal representation of the number 16,777,215 in base 10.
     * It is often used in bitwise operations to extract specific bits or perform bitwise masking.
     *
     * By performing (0xFFFFFF & colorValue), you obtain a value that
     * represents the RGB color without any additional bits.
     * */
    private fun getHexColorByColorInt(color: Int): String {
        return String.format(HEX_COLOR_FORMAT, 0xFFFFFF and color)
    }

    private fun isDarkModeApp(): Boolean {
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        return currentNightMode == AppCompatDelegate.MODE_NIGHT_YES
    }
}