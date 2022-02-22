package com.tokopedia.imagepicker.editor.converter.config

import android.graphics.Color

data class CanvasConfig @JvmOverloads constructor(
    var width: CanvasSize = CanvasSize.WrapContent,
    var height: CanvasSize = CanvasSize.WrapContent,
    var color: Int = Color.WHITE
)