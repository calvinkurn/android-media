package com.tokopedia.imagepicker.editor.watermark.data

import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel

open class TextDefault(
    @ColorInt override var backgroundColor: Int = Color.TRANSPARENT,
    @ColorInt override var textShadowColor: Int = Color.WHITE,
    @ColorInt override var textColor: Int = Color.BLACK,
    @FontRes override var fontTypeId: Int = 0,
    override var textStyle: Paint.Style = Paint.Style.FILL,
    override var textShadowBlurRadius: Float = 0f,
    override var textShadowXOffset: Float = 0f,
    override var textShadowYOffset: Float = 0f,
    override var textSize: Int = 14,
    override var alpha: Int = 100,
    override var text: String = "",
) : TextUIModel, PositionDefault()