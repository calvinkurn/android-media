package com.tokopedia.imagepicker.editor.watermark.uimodel

import android.content.Context
import android.graphics.Bitmap

data class WatermarkUIModel(
    val backgroundImg: Bitmap,
    val watermarkText: WatermarkText,
    val watermarkImg: Bitmap,
    val context: Context
)