package com.tokopedia.product_ar.view

import com.modiface.mfemakeupkit.effects.MFEMakeupProduct

interface ProductArListener {
    fun onVariantClicked(productId: String, isSelected: Boolean, selectedMfeProduct: MFEMakeupProduct)
}