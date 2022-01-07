package com.tokopedia.product_ar.model

import com.modiface.mfemakeupkit.effects.MFEMakeupLook

data class ModifaceUiModel(
        val modifaceProductData: MFEMakeupLook = MFEMakeupLook(),
        val isSelected: Boolean = false,
        val backgroundUrl: String = "",
        val productName: String = "",
        val productId: String = "",
        val modifaceType: String = "",
        val counter: Int? = null
)
