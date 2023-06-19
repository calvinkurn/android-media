package com.tokopedia.media.editor.ui.uimodel

import android.graphics.Typeface
import android.os.Parcelable
import android.text.Layout
import com.tokopedia.media.editor.data.entity.AddTextAlignment
import com.tokopedia.media.editor.data.entity.AddTextPosition
import com.tokopedia.media.editor.data.entity.AddTextStyle
import com.tokopedia.media.editor.data.entity.AddTextTemplateMode
import com.tokopedia.media.editor.data.entity.AddTextToolId
import kotlinx.parcelize.Parcelize

@Parcelize
class EditorAddTextUiModel(
    var textValue: String,
    var textStyle: Int = AddTextStyle.REGULAR.value,
    var textColor: Int = 0,
    var textAlignment: Int = AddTextAlignment.CENTER.value,
    var textPosition: Int = AddTextPosition.BOTTOM.value,
    var textTemplate: Int = AddTextTemplateMode.FREE.value,
    private var textTemplateBackgroundDetail: BackgroundTemplateDetail? = null,
    var textImagePath: String? = null
) : Parcelable {

    // need to encapsulate background template model to prevent error when using `Free Text` but accessing `Latar Text` template
    fun getBackgroundTemplate(): BackgroundTemplateDetail? {
        return if (textTemplate != AddTextTemplateMode.BACKGROUND.value) {
            return null
        } else {
            textTemplateBackgroundDetail
        }
    }

    fun setBackgroundTemplate(backgroundTemplate: BackgroundTemplateDetail) {
        textTemplateBackgroundDetail = backgroundTemplate
        if (textTemplate == AddTextTemplateMode.BACKGROUND.value) {
            // if using background, text can be only on bottom or right
            if (textPosition == AddTextPosition.TOP.value || textPosition == AddTextPosition.LEFT.value) {
                textPosition = AddTextPosition.BOTTOM.value
            }
        }
    }

    fun getLayoutAlignment(): Layout.Alignment {
        return when (textAlignment) {
            AddTextAlignment.CENTER.value -> Layout.Alignment.ALIGN_CENTER
            AddTextAlignment.LEFT.value -> Layout.Alignment.ALIGN_NORMAL
            else -> Layout.Alignment.ALIGN_OPPOSITE
        }
    }

    /**
     * @return one of [Typeface.BOLD, Typeface.ITALIC, Typeface.NORMAL]
     */
    fun getTypeFaceStyle(): Int {
        return when (textStyle) {
            AddTextStyle.BOLD.value -> Typeface.BOLD
            AddTextStyle.ITALIC.value -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }
    }
}

@Parcelize
data class BackgroundTemplateDetail(
    val addTextBackgroundColor: Int,
    val addTextBackgroundModel: Int
) : Parcelable
