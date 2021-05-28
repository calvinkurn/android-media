package com.tokopedia.imagepicker.editor.watermark.uimodel

import androidx.annotation.FloatRange

data class WatermarkPosition(
    @FloatRange(from = 0.0, to = 1.0) var positionX: Double = 0.0,
    @FloatRange(from = 0.0, to = 1.0) var positionY: Double = 0.0,
    var rotation: Double = 0.0
)