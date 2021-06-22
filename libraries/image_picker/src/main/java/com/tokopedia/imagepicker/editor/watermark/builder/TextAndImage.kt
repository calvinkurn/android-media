package com.tokopedia.imagepicker.editor.watermark.builder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import com.tokopedia.imagepicker.editor.watermark.data.ImageDefault
import com.tokopedia.imagepicker.editor.watermark.data.TextDefault
import com.tokopedia.imagepicker.editor.watermark.entity.TextAndImageUIModel

data class TextAndImage(
    // internal default value
    internal val imageDefault: ImageDefault = ImageDefault(),
    internal val textDefault: TextDefault = TextDefault(),

    // base properties
    override var position: Position = imageDefault.position,

    // image properties
    override var image: Bitmap? = imageDefault.image,
    override var imageDrawable: Int = imageDefault.imageDrawable,
    override var context: Context? = imageDefault.context,
    override var imageSize: Double = imageDefault.imageSize,
    override var imageAlpha: Int = 100,

    // text properties
    override var text: String = textDefault.text,
    override var textColor: Int = textDefault.textColor,
    override var textAlpha: Int = textDefault.textAlpha,
    override var textSize: Int = textDefault.textSize,
    override var backgroundColor: Int = textDefault.backgroundColor,
    override var textStyle: Paint.Style = textDefault.textStyle,
    override var fontName: String = textDefault.fontName,
    override var textShadowBlurRadius: Float = textDefault.textShadowBlurRadius,
    override var textShadowXOffset: Float = textDefault.textShadowXOffset,
    override var textShadowYOffset: Float = textDefault.textShadowYOffset,
    override var textShadowColor: Int = textDefault.textShadowColor
) : TextAndImageUIModel