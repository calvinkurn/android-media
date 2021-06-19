package com.tokopedia.imagepicker.editor.watermark.entity

import android.graphics.Color
import android.graphics.Paint
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.FontRes

data class Text(
    var text: String = "",
    var textAlpha: Int = 50,
    var textSize: Int = 20,
    @ColorInt var textColor: Int = Color.BLACK,
    @ColorInt var backgroundColor: Int = Color.TRANSPARENT,
    var textStyle: Paint.Style = Paint.Style.FILL,
    @FontRes var typeFaceId: Int = 0,
    var textShadowBlurRadius: Float = 0f,
    var textShadowXOffset: Float = 0f,
    var textShadowYOffset: Float = 0f,
    @ColorInt var textShadowColor: Int = Color.WHITE,
    var position: Position = Position()
) {

    fun textSize(value: Int) = apply {
        this.textSize = value
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

    fun textAlpha(value: Int) = apply {
        this.textAlpha = value
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