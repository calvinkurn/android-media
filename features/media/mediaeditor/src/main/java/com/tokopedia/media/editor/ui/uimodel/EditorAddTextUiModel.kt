package com.tokopedia.media.editor.ui.uimodel

import android.graphics.Typeface
import android.os.Parcelable
import android.text.Layout
import com.tokopedia.media.editor.ui.adapter.AddTextToolAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
class EditorAddTextUiModel(
    var textValue: String,
    var textStyle: Int = TEXT_STYLE_REGULAR,
    var textColor: Int = 0,
    var textAlignment: Int = TEXT_ALIGNMENT_CENTER,
    var textPosition: Int = TEXT_POSITION_BOTTOM,
    var textTemplate: Int = TEXT_TEMPLATE_FREE,
    private var textTemplateLatar: LatarTemplateDetail? = null
) : Parcelable {

    // need to encapsulate latar template model to prevent error when using `Free Text` but accessing `Latar Text` template
    fun getLatarTemplate(): LatarTemplateDetail? {
        return if (textTemplate != TEXT_TEMPLATE_BACKGROUND) {
            return null
        } else {
            textTemplateLatar
        }
    }

    fun setLatarTemplate(latarTemplate: LatarTemplateDetail) {
        textTemplateLatar = latarTemplate
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

        const val TEXT_POSITION_LEFT = 0
        const val TEXT_POSITION_RIGHT = 1
        const val TEXT_POSITION_TOP = 2
        const val TEXT_POSITION_BOTTOM = 3

        const val TEXT_TEMPLATE_FREE = AddTextToolAdapter.FREE_TEXT_INDEX
        const val TEXT_TEMPLATE_BACKGROUND = AddTextToolAdapter.BACKGROUND_TEXT_INDEX

        // --- if edit this line please check AddTextLatarBottomSheet
        const val TEXT_LATAR_TEMPLATE_FULL = 0
        const val TEXT_LATAR_TEMPLATE_FLOATING = 1
        const val TEXT_LATAR_TEMPLATE_SIDE_CUT = 2

        const val TEXT_LATAR_TEMPLATE_BLACK = 0
        const val TEXT_LATAR_TEMPLATE_WHITE = 1
        // ---
    }
}

@Parcelize
data class LatarTemplateDetail(
    val latarColor: Int,
    val latarModel: Int
) : Parcelable
