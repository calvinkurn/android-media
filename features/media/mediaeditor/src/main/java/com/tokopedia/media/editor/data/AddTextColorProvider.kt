package com.tokopedia.media.editor.data

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
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
    private val black2 = ContextCompat.getColor(context, editorR.color.dms_editor_add_text_black)
    private val white2 = ContextCompat.getColor(context, editorR.color.dms_editor_add_text_white)

    private val blackText2 = context.getString(editorR.string.add_text_color_name_black)
    private val whiteText2 = context.getString(editorR.string.add_text_color_name_white)

    private val listOfTextColor = listOf(
        black2,
        white2
    )

    private val listOfTextWithBackgroundColor = mapOf(
        black2 to blackText2,
        white2 to whiteText2
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
            black2 -> white2
            else -> black2
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
            black2 -> blackText2
            white2 -> whiteText2
            else -> color.toString()
        }
    }

    override fun drawableToWhite(drawable: Drawable) {
        implementDrawableColor(drawable, white2)
    }
}
