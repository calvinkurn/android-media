package com.tokopedia.media.editor.data.entity

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.graphics.toColorInt
import javax.inject.Inject

interface AddTextColorManager{
    val listOfTextColor: List<String>
    val listOfTextWithBackgroundColor: List<Pair<String, String>>
    fun getTextColorIndex(color: String): Int
    fun getTextColorOnBackgroundMode(backgroundColor: String): String
    fun implementDrawableColor(drawable: Drawable, color: String)
    fun getTextFromHex(hexColor: String?): String
}

class AddTextColorManagerImpl @Inject constructor(): AddTextColorManager {
    private val black = "#000000"
    private val white = "#FFFFFF"

    private val blackText = "Hitam"
    private val whiteText = "Putih"

    override val listOfTextColor = listOf(
        black,
        white
    )

    override val listOfTextWithBackgroundColor = listOf(
        Pair(black, blackText),
        Pair(white, whiteText)
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

    override fun getTextFromHex(hexColor: String?): String {
        return when(hexColor){
            black -> blackText
            white -> whiteText
            else -> hexColor ?: ""
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
