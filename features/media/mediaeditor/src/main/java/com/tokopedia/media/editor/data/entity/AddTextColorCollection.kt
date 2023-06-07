package com.tokopedia.media.editor.data.entity

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.graphics.toColorInt
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import javax.inject.Inject

interface AddTextColorCollection{
    val listOfTextColor: List<String>
    val listOfTextWithBackgroundColor: List<String>
    val listOfTextWithBackgroundColorName: List<String>
    fun getTextColorIndex(color: String): Int
    fun getTextColorOnBackgroundMode(backgroundColor: String): String
    fun implementDrawableColor(drawable: Drawable, color: String)
}

class AddTextColorCollectionImpl @Inject constructor(): AddTextColorCollection {
    private val black = "#000000"
    private val white = "#FFFFFF"

    override val listOfTextColor = listOf(
        black,
        white
    )

    override val listOfTextWithBackgroundColor = listOf(
        black,
        white
    )

    override val listOfTextWithBackgroundColorName = listOf(
        "Hitam",
        "Putih"
    )

    override fun getTextColorIndex(color: String): Int {
        return listOfTextColor.indexOf(color)
    }

    override fun getTextColorOnBackgroundMode(backgroundColor: String): String {
        // if background white, text black. vice versa
        return when (backgroundColor) {
            black -> white
            else -> black
        }
    }

    override fun implementDrawableColor(drawable: Drawable, color: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color.toColorInt(), BlendMode.SRC_ATOP)
        } else {
            drawable.setColorFilter(color.toColorInt(), PorterDuff.Mode.SRC_ATOP)
        }
    }
}
