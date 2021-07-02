package com.tokopedia.imagepicker.editor.watermark.data

import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.takeAndEllipsizeOf

open class TextDefault(
    @ColorInt override var backgroundColor: Int = Color.TRANSPARENT,
    @ColorInt override var textShadowColor: Int = Color.WHITE,
    @ColorInt override var textColor: Int = Color.WHITE,
    override var fontName: String = "NunitoSansExtraBold.ttf",
    override var textStyle: Paint.Style = Paint.Style.FILL,
    override var textShadowBlurRadius: Float = 0f,
    override var textShadowXOffset: Float = 0f,
    override var textShadowYOffset: Float = 0f,
    override var textAlpha: Int = 80,
    override var textSize: Int = 24,
    override var text: String = "",
) : TextUIModel, PositionDefault()