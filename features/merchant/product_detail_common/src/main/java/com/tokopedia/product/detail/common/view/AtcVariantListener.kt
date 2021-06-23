package com.tokopedia.product.detail.common.view

import android.view.View
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 06/05/21
 */
interface AtcVariantListener {
    fun onVariantClicked(variantOptions: VariantOptionWithAttribute)
    fun getStockWording(): String = ""

    fun onVariantGuideLineClicked(url: String) {}
    fun onSelectionChanged(view: View, position: Int) {}
    fun onVariantGuideLineHide(): Boolean = false
    fun onVariantImageClicked(url: String){}
    fun onVariantEmptyAndSelectedClicked(){}

    fun onQuantityUpdate(quantity: Int, productId: String) {}
    fun onClickRefresh() {}
    fun isTokonow(): Boolean = false
}