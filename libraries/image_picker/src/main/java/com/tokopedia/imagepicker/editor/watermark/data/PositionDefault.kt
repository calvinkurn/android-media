package com.tokopedia.imagepicker.editor.watermark.data

import androidx.annotation.FloatRange
import com.tokopedia.imagepicker.editor.watermark.builder.Position
import com.tokopedia.imagepicker.editor.watermark.entity.PositionUIModel

open class PositionDefault(
    val position: Position = Position().apply {
        this.positionX = 0.5
        this.positionY = 0.5
        this.rotation = (-30).toDouble()
    },

    @FloatRange(from = 0.0, to = 1.0)
    override var positionX: Double = position.positionX,

    @FloatRange(from = 0.0, to = 1.0)
    override var positionY: Double = position.positionY,

    override var rotation: Double = position.rotation
) : PositionUIModel