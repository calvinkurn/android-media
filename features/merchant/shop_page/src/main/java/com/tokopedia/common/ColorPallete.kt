package com.tokopedia.common

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.tokopedia.unifycomponents.Label

/**
 * This Color Pallete is used to retain color in light mode/dark mode
 * The value is supplied from backend
 *
 * How to use:
 * textView.setRetainTextColor(colorPalleteObj, ColorType.PRIMARY_TEXT, <<optionalDefaultColor>>)
 */
data class ColorPallete(
    var primaryTextColor: String? = null, // 212121 or FFFFFF
    var primaryBackground: String? = null, // 00FFFFFF transparent, background under primary text
    var secondaryTextColor: String? = null, // 6D7588
    var slashedTextColor: String? = null, // #F94D63 for slash price
    var slashedBackgroundColor: String? = null, // for slash price, background under slashed text
    var buttonAccent: String? = null, //#00AA5B green for button
    var white: String? = null, // #FFFFFF white for background under button, text in button, etc
    var darkGrey: String? = null, // #6D7588 for title text, etx
    var black: String? = null, // #2E3137 for tinting standard icon, ex: plus button icon
) {
    companion object {
        fun safeParseColor(color: String?): Int? {
            if (color == null) {
                return null
            }
            return try {
                Color.parseColor(color)
            } catch (throwable: Throwable) {
                null
            }
        }
    }

    enum class ColorType {
        PRIMARY_TEXT,
        SECONDARY_TEXT,
        SLASHED_TEXT,
        BUTTON_ACCENT,
        PRIMARY_BG,
        SLASHED_BG,
        WHITE,
        DARK_GREY,
        BLACK
    }

    fun getColor(type: ColorType): String? {
        return when (type) {
            ColorType.PRIMARY_TEXT -> primaryTextColor
            ColorType.PRIMARY_BG -> primaryBackground
            ColorType.SECONDARY_TEXT -> secondaryTextColor
            ColorType.SLASHED_TEXT -> slashedTextColor
            ColorType.SLASHED_BG -> slashedBackgroundColor
            ColorType.BUTTON_ACCENT -> buttonAccent
            ColorType.WHITE -> white
            ColorType.DARK_GREY -> darkGrey
            ColorType.BLACK -> black
        }
    }

}

fun ColorPallete?.getColorInt(colorType: ColorPallete.ColorType): Int? {
    if (this == null) return null
    val color = getColor(colorType) ?: return null
    val colorInt = ColorPallete.safeParseColor(color) ?: return null
    return colorInt
}

fun setRetainColor(
    colorPallete: ColorPallete?,
    colorType: ColorPallete.ColorType,
    colorDefault: Int?,
    functionToRun: (Int) -> Unit
) {
    val colorInt = colorPallete?.getColorInt(colorType)
    if (colorInt == null) {
        if (colorDefault != null) {
            functionToRun.invoke(colorDefault)
        }
    } else {
        functionToRun.invoke(colorInt)
    }
}

fun TextView.setRetainTextColor(
    colorPallete: ColorPallete?,
    colorType: ColorPallete.ColorType,
    colorDefault: Int? = null
) {
    setRetainColor(colorPallete, colorType, colorDefault) { c ->
        this.setTextColor(c)
    }
}

fun Label.setRetainBackgroundColor(
    colorPallete: ColorPallete?,
    colorType: ColorPallete.ColorType,
    colorDefault: Int? = null
) {
    setRetainColor(colorPallete, colorType, colorDefault) { c ->
        background.colorFilter = PorterDuffColorFilter(c, PorterDuff.Mode.SRC_ATOP)
    }
}

fun CardView.setRetainCardBackgroundColor(
    colorPallete: ColorPallete?,
    colorType: ColorPallete.ColorType,
    colorDefault: Int? = null
) {
    setRetainColor(colorPallete, colorType, colorDefault) { c ->
        this.setCardBackgroundColor(c)
    }
}

fun ImageView.setRetainColorFilter(
    colorPallete: ColorPallete?,
    colorType: ColorPallete.ColorType,
    colorDefault: Int? = null
) {
    setRetainColor(colorPallete, colorType, colorDefault) { c ->
        this.setColorFilter(c, PorterDuff.Mode.SRC_ATOP)
    }
}