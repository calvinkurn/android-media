package com.tokopedia.product_ar.model.state

import android.graphics.Bitmap

data class ModifaceImageGridState(
        val mode: ImageMapMode = ImageMapMode.APPEND,
        val imagesBitmap: List<Bitmap> = listOf(),
        val spanSize: Int = 1,
        val removePosition: Int = -1
)

enum class ImageMapMode {
    APPEND,
    REMOVE
}