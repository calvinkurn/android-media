package com.tokopedia.imagepicker.editor.watermark.data

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import com.tokopedia.imagepicker.editor.watermark.builder.Position
import com.tokopedia.imagepicker.editor.watermark.entity.ImageUIModel

open class ImageDefault(
    override var image: Bitmap? = null,
    @DrawableRes override var imageDrawable: Int = 0,
    override var alpha: Int = 50,
    override var context: Context? = null,
    @FloatRange(from = 0.0, to = 1.0) override var size: Double = 0.2,
    override var position: Position = Position().apply {
        this.positionX = 0.5
        this.positionY = 0.5
        this.rotation = (-30).toDouble()
    },
) : ImageUIModel