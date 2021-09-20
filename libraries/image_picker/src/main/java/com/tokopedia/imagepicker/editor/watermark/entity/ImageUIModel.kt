package com.tokopedia.imagepicker.editor.watermark.entity

import android.content.Context
import android.graphics.Bitmap

interface ImageUIModel : BaseWatermark {
    var context: Context?
    var imageAlpha: Int
    var image: Bitmap?
    var imageSize: Double
    var imageDrawable: Int
}