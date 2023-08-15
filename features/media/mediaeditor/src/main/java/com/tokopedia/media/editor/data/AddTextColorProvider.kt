package com.tokopedia.media.editor.data

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import javax.inject.Inject
import com.tokopedia.media.editor.R as editorR

interface AddTextColorProvider{
    fun getListOfTextColor(): List<Int>
    fun getListOfTextWithBackgroundColor(): Map<Int, String>
    fun getTextColorIndex(color: Int): Int
    fun getTextColorOnBackgroundMode(backgroundColor: Int): Int
    fun implementDrawableColor(drawable: Drawable, color: Int)
    fun drawableToWhite(drawable: Drawable)

    // used by tracker to convert addText text color to color name
    fun getTextColorName(color: Int): String
}

class AddTextColorProviderImpl @Inject constructor(
    val context: Context
): AddTextColorProvider {
    private val blackColor = ContextCompat.getColor(context, editorR.color.dms_editor_add_text_black)
    private val whiteColor = ContextCompat.getColor(context, editorR.color.dms_editor_add_text_white)

    // used on UI
    private val blackColorText = context.getString(editorR.string.add_text_color_name_black)
    private val whiteColorText = context.getString(editorR.string.add_text_color_name_white)

    // used on tracker
    private val blackTextIdn = context.getString(editorR.string.add_text_color_name_black_idn)
    private val whiteTextIdn = context.getString(editorR.string.add_text_color_name_white_idn)

    private val listOfTextColor = listOf(
        blackColor,
        whiteColor
    )

    private val listOfTextWithBackgroundColor = mapOf(
        blackColor to blackColorText,
        whiteColor to whiteColorText
    )

    override fun getListOfTextColor(): List<Int> {
        return listOfTextColor
    }

    override fun getListOfTextWithBackgroundColor(): Map<Int, String> {
        return listOfTextWithBackgroundColor
    }

    override fun getTextColorIndex(color: Int): Int {
        return listOfTextColor.indexOf(color)
    }

    override fun getTextColorOnBackgroundMode(backgroundColor: Int): Int {
        // if background white, text black. vice versa
        return when (backgroundColor) {
            blackColor -> whiteColor
            else -> blackColor
        }
    }

    override fun implementDrawableColor(drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    override fun getTextColorName(color: Int): String {
        return when(color){
            blackColor -> blackTextIdn
            whiteColor -> whiteTextIdn
            else -> color.toString()
        }
    }

    override fun drawableToWhite(drawable: Drawable) {
        implementDrawableColor(drawable, whiteColor)
    }
}
