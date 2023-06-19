package com.tokopedia.media.editor.ui.uimodel

import android.graphics.Typeface
import android.os.Parcelable
import android.text.Layout
import com.tokopedia.media.editor.data.entity.AddTextAlignment
import com.tokopedia.media.editor.data.entity.AddTextBackgroundTemplate
import com.tokopedia.media.editor.data.entity.AddTextPosition
import com.tokopedia.media.editor.data.entity.AddTextStyle
import com.tokopedia.media.editor.data.entity.AddTextTemplateMode
import kotlinx.parcelize.Parcelize

@Parcelize
class EditorAddTextUiModel(
    var textValue: String,
    var textStyle: AddTextStyle = AddTextStyle.REGULAR,
    var textColor: Int = 0,
    var textAlignment: AddTextAlignment = AddTextAlignment.CENTER,
    var textPosition: AddTextPosition = AddTextPosition.BOTTOM,
    var textTemplate: AddTextTemplateMode = AddTextTemplateMode.FREE,
    private var textTemplateBackgroundDetail: BackgroundTemplateDetail? = null,
    var textImagePath: String? = null
) : Parcelable {

    // need to encapsulate background template model to prevent error when using `Free Text` but accessing `Latar Text` template
    fun getBackgroundTemplate(): BackgroundTemplateDetail? {
        return if (textTemplate != AddTextTemplateMode.BACKGROUND) {
            return null
        } else {
            textTemplateBackgroundDetail
        }
    }

    fun setBackgroundTemplate(backgroundTemplate: BackgroundTemplateDetail) {
        textTemplateBackgroundDetail = backgroundTemplate
        if (textTemplate == AddTextTemplateMode.BACKGROUND) {
            // if using background, text can be only on bottom or right
            if (textPosition == AddTextPosition.TOP || textPosition == AddTextPosition.LEFT) {
                textPosition = AddTextPosition.BOTTOM
            }
        }
    }

    fun getLayoutAlignment(): Layout.Alignment {
        return when (textAlignment) {
            AddTextAlignment.CENTER -> Layout.Alignment.ALIGN_CENTER
            AddTextAlignment.LEFT -> Layout.Alignment.ALIGN_NORMAL
            else -> Layout.Alignment.ALIGN_OPPOSITE
        }
    }

    /**
     * @return one of [Typeface.BOLD, Typeface.ITALIC, Typeface.NORMAL]
     */
    fun getTypeFaceStyle(): Int {
        return when (textStyle) {
            AddTextStyle.BOLD -> Typeface.BOLD
            AddTextStyle.ITALIC -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }
    }
}

@Parcelize
data class BackgroundTemplateDetail(
    val addTextBackgroundColor: Int,
    val addTextBackgroundModel: AddTextBackgroundTemplate
) : Parcelable
