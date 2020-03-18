package com.tokopedia.variant_common.view

import android.view.View
import com.tokopedia.variant_common.model.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 08/03/20
 */
interface ProductVariantListener {
    fun onVariantClicked(variantOptions: VariantOptionWithAttribute)
    fun onVariantGuideLineClicked(url: String)
    fun getStockWording(): String

    fun onSelectionChanged(view: View, position: Int) {}
}