package com.tokopedia.imagepicker.editor.watermark.data

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import com.tokopedia.imagepicker.editor.watermark.entity.ImageUIModel

open class ImageDefault(
    override var image: Bitmap? = null,
    override var context: Context? = null,
    override var imageAlpha: Int = 120,
    @DrawableRes override var imageDrawable: Int = 0,
    @FloatRange(from = 0.0, to = 1.0) override var imageSize: Double = 0.15
) : ImageUIModel, PositionDefault()