package com.tokopedia.imagepicker.editor.watermark.builder

import android.widget.EditText
import android.widget.TextView
import com.tokopedia.imagepicker.editor.watermark.data.TextDefault
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel

class Text : TextUIModel, TextDefault() {

    fun setTextShadow(
        blurRadius: Float,
        shadowXOffset: Float,
        shadowYOffset: Float,
        shadowColor: Int
    ) {
        textShadowBlurRadius = blurRadius
        textShadowXOffset = shadowXOffset
        textShadowYOffset = shadowYOffset
        textShadowColor = shadowColor
    }

    fun textFromTextView(textView: TextView) {
        text = textView.text.toString()
    }

    fun textFromEditText(editText: EditText) {
        text = editText.text.toString()
    }

}