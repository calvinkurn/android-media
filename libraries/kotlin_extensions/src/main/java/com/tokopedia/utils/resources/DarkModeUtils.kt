package com.tokopedia.utils.resources

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.tokopedia.unifyprinciples.R as UnifyRoot
import com.tokopedia.unifyprinciples.stringToUnifyColor
import java.lang.reflect.Field
import java.util.regex.Pattern

fun Context.isDarkMode(): Boolean {
    return try {
        when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    } catch (ignored: Exception) {
        false
    }
}

fun isDarkModeApp(): Boolean {
    val currentNightMode = AppCompatDelegate.getDefaultNightMode()
    return currentNightMode == AppCompatDelegate.MODE_NIGHT_YES
}

object DarkModeUtils {

    private const val HEX_COLOR_REGEX = "#([a-fA-F0-9]{8}|[a-fA-F0-9]{6}|[a-fA-F0-9]{3})"
    private const val HEX_COLOR_FORMAT = "#%06X"

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
            val unifyColorName = unify.unifyColorName
            val dmsColor = getDmsColorByName(context, unifyColorName)
            if (dmsColor != null) {
                return getHexColorByColorInt(dmsColor)
            } else {
                val unifyColor = unify.unifyColor
                if (unifyColor != null) {
                    return getHexColorByColorInt(unifyColor)
                }
                return hexColor
            }
        }
        return hexColor
    }

    /**The ContextCompat.getColor() method retrieves the color value as an integer.
     * We then convert this integer to a hex color string using String.format() and
     * the "%06X" format specifier.
     * */
    private fun getHexColorByColorInt(color: Int): String {
        return String.format(HEX_COLOR_FORMAT, 0xFFFFFF and color)
    }

    private fun getDmsColorByName(context: Context, colorName: String?): Int? {
        runCatching {
            val res = UnifyRoot.color::class.java
            val field: Field = res.getField(colorName ?: return@runCatching)
            val color: Int = field.getInt(null)
            return ContextCompat.getColor(context, color)
        }
        return null
    }
}