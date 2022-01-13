package com.tokopedia.product_ar.model.state

import com.tokopedia.product_ar.model.ComparissonImageUiModel

data class ModifaceImageGridState(
        val mode: ImageMapMode = ImageMapMode.APPEND,
        val imagesBitmap: List<ComparissonImageUiModel> = listOf(),
        val spanSize: Int = 1,
        val removePosition: Int = -1
)

enum class ImageMapMode {
    APPEND,
    REMOVE
}