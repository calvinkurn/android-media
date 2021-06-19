package com.tokopedia.imagepicker.editor.watermark.data

import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import com.tokopedia.imagepicker.editor.watermark.builder.Position
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel

open class TextDefault(
    override var text: String = "",
    @ColorInt override var textColor: Int = Color.BLACK,
    @ColorInt override var backgroundColor: Int = Color.TRANSPARENT,
    override var textStyle: Paint.Style = Paint.Style.FILL,
    @FontRes override var typeFaceId: Int = 0,
    override var textShadowBlurRadius: Float = 0f,
    override var textShadowXOffset: Float = 0f,
    override var textShadowYOffset: Float = 0f,
    @ColorInt override var textShadowColor: Int = Color.WHITE,
    override var position: Position = Position().apply {
        this.positionX = 0.5
        this.positionY = 0.5
        this.rotation = (-30).toDouble()
    },
    override var alpha: Int = 50,
    override var size: Double = 20.0
) : TextUIModel