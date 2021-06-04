package com.tokopedia.imagepicker.editor.watermark.uimodel

import android.graphics.Color
import android.graphics.Paint
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.FontRes

data class WatermarkText(
    var text: String = "",
    var alpha: Int = 50,
    var size: Int = 20,
    @ColorInt var color: Int = Color.BLACK,
    @ColorInt var backgroundColor: Int = Color.TRANSPARENT,
    var style: Paint.Style = Paint.Style.FILL,
    @FontRes var typeFaceId: Int = 0,
    var textShadowBlurRadius: Float = 0f,
    var textShadowXOffset: Float = 0f,
    var textShadowYOffset: Float = 0f,
    @ColorInt var textShadowColor: Int = Color.WHITE,
    var position: WatermarkPosition = WatermarkPosition()
) {

    fun textSize(value: Int) = apply {
        this.size = value
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
        this.alpha = value
    }

    fun textColor(value: Int) = apply {
        this.color = value
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