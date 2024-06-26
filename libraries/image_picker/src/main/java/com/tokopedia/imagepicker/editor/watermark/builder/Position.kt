package com.tokopedia.imagepicker.editor.watermark.builder

import androidx.annotation.FloatRange

class Position(
    @FloatRange(from = 0.0, to = 1.0) var positionX: Double = 0.0,
    @FloatRange(from = 0.0, to = 1.0) var positionY: Double = 0.0,
    var rotation: Double = 0.0
)