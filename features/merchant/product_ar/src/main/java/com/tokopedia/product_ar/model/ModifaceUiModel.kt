package com.tokopedia.product_ar.model

import com.modiface.mfemakeupkit.effects.MFEMakeupProduct

data class ModifaceUiModel(
        val modifaceProductData: MFEMakeupProduct = MFEMakeupProduct(),
        val isSelected: Boolean = false,
        val backgroundUrl: String = "",
        val productName: String = "",
        val productId: String = "",
        val modifaceType: String = ""
)
