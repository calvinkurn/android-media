package com.tokopedia.imagepicker.editor.watermark.entity

import com.tokopedia.imagepicker.editor.watermark.builder.Position

interface BaseWatermark {
    var alpha: Int
    var position: Position
}