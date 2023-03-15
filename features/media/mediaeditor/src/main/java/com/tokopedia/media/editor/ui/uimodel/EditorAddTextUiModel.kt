package com.tokopedia.media.editor.ui.uimodel

import android.content.Context
import android.graphics.Typeface
import android.os.Parcelable
import android.text.Layout
import androidx.core.content.ContextCompat
import kotlinx.parcelize.Parcelize

@Parcelize
class EditorAddTextUiModel(
    val textValue: String,
    val textStyle: Int = TEXT_STYLE_REGULAR,
    val textColor: Int = 0,
    val textAlignment: Int = TEXT_ALIGNMENT_CENTER,
    val textPosition: Int = TEXT_POSITION_LEFT
) : Parcelable {
    fun getColor(context: Context?): Int {
        if (context == null) return 0
        return ContextCompat.getColor(context, textColor)
    }

    fun getLayoutAlignment(): Layout.Alignment {
        return when (textAlignment) {
            TEXT_ALIGNMENT_CENTER -> Layout.Alignment.ALIGN_CENTER
            TEXT_ALIGNMENT_LEFT -> Layout.Alignment.ALIGN_NORMAL
            else -> Layout.Alignment.ALIGN_OPPOSITE
        }
    }

    /**
     * @return one of [Typeface.BOLD, Typeface.ITALIC, Typeface.NORMAL]
     */
    fun getTypeFaceStyle(): Int {
        return when (textStyle) {
            TEXT_STYLE_BOLD -> Typeface.BOLD
            TEXT_STYLE_ITALIC -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }
    }

    companion object {
        const val TEXT_ALIGNMENT_CENTER = 0
        const val TEXT_ALIGNMENT_LEFT = 1
        const val TEXT_ALIGNMENT_RIGHT = 2

        const val TEXT_STYLE_REGULAR = 0
        const val TEXT_STYLE_BOLD = 1
        const val TEXT_STYLE_ITALIC = 2

        const val TEXT_POSITION_BOTTOM = 0
        const val TEXT_POSITION_TOP = 1
        const val TEXT_POSITION_LEFT = 2
        const val TEXT_POSITION_RIGHT = 3
    }
}
