package com.tokopedia.product_ar.view

import com.modiface.mfemakeupkit.effects.MFEMakeupProduct

interface ProductArListener {
    fun onVariantClicked(productId: String,
                         productName: String,
                         isSelected: Boolean,
                         selectedMfeProduct: MFEMakeupProduct)

    fun onButtonClicked(productId: String)
}