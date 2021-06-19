package com.tokopedia.imagepicker.editor.watermark.builder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import com.tokopedia.imagepicker.editor.watermark.data.ImageDefault
import com.tokopedia.imagepicker.editor.watermark.data.TextDefault
import com.tokopedia.imagepicker.editor.watermark.entity.*
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.getBitmapFromDrawable

data class TextAndImage(
    // internal default value
    internal val imageDefault: ImageDefault = ImageDefault(),
    internal val textDefault: TextDefault = TextDefault(),

    // watermark positioning
    override var position: Position = Position(),

    // image properties
    override var image: Bitmap? = imageDefault.image,
    override var imageDrawable: Int = imageDefault.imageDrawable,
    override var alpha: Int = imageDefault.alpha,
    override var context: Context? = imageDefault.context,
    override var size: Double = imageDefault.size,

    // text properties
    override var text: String = textDefault.text,
    override var textColor: Int = textDefault.textColor,
    override var backgroundColor: Int = textDefault.backgroundColor,
    override var textStyle: Paint.Style = textDefault.textStyle,
    override var typeFaceId: Int = textDefault.typeFaceId,
    override var textShadowBlurRadius: Float = textDefault.textShadowBlurRadius,
    override var textShadowXOffset: Float = textDefault.textShadowXOffset,
    override var textShadowYOffset: Float = textDefault.textShadowYOffset,
    override var textShadowColor: Int = textDefault.textShadowColor
) : TextAndImageUIModel {

    fun alpha(value: Int) = apply {
        this.alpha = value
    }

    fun contentText(value: String) = apply {
        this.text = value
    }

    fun size(value: Int) = apply {
        this.size = value.toDouble()
    }

    fun textColor(value: Int) = apply {
        this.textColor = value
    }

    fun imageDrawable(value: Int) = apply {
        this.imageDrawable = value

        if (imageDrawable != 0) {
            image = getBitmapFromDrawable(context, imageDrawable)
        }
    }

    fun rotation(value: Double) = apply {
        this.position.rotation = value
    }

    fun positionX(value: Double) = apply {
        this.position.positionX = value
    }

    fun positionY(value: Double) = apply {
        this.position.positionY = value
    }

}