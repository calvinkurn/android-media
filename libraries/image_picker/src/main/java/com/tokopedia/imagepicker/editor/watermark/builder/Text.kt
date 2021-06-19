package com.tokopedia.imagepicker.editor.watermark.builder

import android.widget.EditText
import android.widget.TextView
import com.tokopedia.imagepicker.editor.watermark.data.TextDefault
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel

class Text : TextDefault(), TextUIModel {

    fun size(value: Int) = apply {
        this.size = value.toDouble()
    }

    fun rotation(value: Double) = apply {
        this.position.rotation = value
    }

    fun contentText(value: String) = apply {
        this.text = value
    }

    fun positionX(value: Double) = apply {
        this.position.positionX = value
    }

    fun positionY(value: Double) = apply {
        this.position.positionY = value
    }

    fun alpha(value: Int) = apply {
        this.alpha = value
    }

    fun textColor(value: Int) = apply {
        this.textColor = value
    }

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