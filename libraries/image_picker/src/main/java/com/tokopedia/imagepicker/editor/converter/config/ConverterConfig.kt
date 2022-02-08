package com.tokopedia.imagepicker.editor.converter.config

import android.graphics.Bitmap

class ConverterConfig @JvmOverloads constructor(
    var canvasConfig: CanvasConfig = CanvasConfig(),
    var bitmapConfig: Bitmap.Config = Bitmap.Config.ARGB_8888
)