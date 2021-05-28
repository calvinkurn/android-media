package com.tokopedia.imagepicker.editor.watermark.uimodel

import android.graphics.Bitmap

data class DetectionReturnValue(
    var watermarkBitmap: Bitmap? = null,
    var watermarkString: String = ""
)