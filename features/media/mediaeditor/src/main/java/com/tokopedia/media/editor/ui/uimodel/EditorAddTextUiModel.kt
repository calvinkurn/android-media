package com.tokopedia.media.editor.ui.uimodel

import android.content.Context
import android.os.Parcelable
import androidx.core.content.ContextCompat
import kotlinx.parcelize.Parcelize

@Parcelize
class EditorAddTextUiModel(
    val textStyle: Int = TEXT_STYLE_REGULAR,
    val textColor: Int = 0,
    val textAlignment: Int = TEXT_ALIGNMENT_CENTER,
    val textValue: String
) : Parcelable {
    fun getColor(context: Context?): Int {
        if (context == null) return 0
        return ContextCompat.getColor(context, textColor)
    }

    companion object {
        const val TEXT_ALIGNMENT_CENTER = 0
        const val TEXT_ALIGNMENT_LEFT = 1
        const val TEXT_ALIGNMENT_RIGHT = 2

        const val TEXT_STYLE_REGULAR = 0
        const val TEXT_STYLE_BOLD = 1
        const val TEXT_STYLE_ITALIC = 2
    }

    override fun describeContents(): Int {
        return 0
    }
}
