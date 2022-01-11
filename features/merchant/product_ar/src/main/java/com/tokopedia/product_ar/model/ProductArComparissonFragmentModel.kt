package com.tokopedia.product_ar.model

import android.graphics.Bitmap

data class ProductArComparissonFragmentModel(
        val processedPhoto: Bitmap? = null,
        val originalPhoto: Bitmap? = null,
        val modifaceUiModel: List<ModifaceUiModel> = listOf()
)
