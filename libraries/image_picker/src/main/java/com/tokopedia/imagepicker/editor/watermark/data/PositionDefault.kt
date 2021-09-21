package com.tokopedia.imagepicker.editor.watermark.data

import com.tokopedia.imagepicker.editor.watermark.builder.Position

open class PositionDefault(
    var position: Position = Position().apply {
        this.positionX = 0.5
        this.positionY = 0.5
        this.rotation = (-30).toDouble()
    }
)