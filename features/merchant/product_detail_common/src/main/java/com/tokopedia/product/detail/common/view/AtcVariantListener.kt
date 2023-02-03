package com.tokopedia.product.detail.common.view

import android.view.View
import com.tokopedia.product.detail.common.VariantConstant.IGNORE_STATE
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 06/05/21
 */
interface AtcVariantListener {
    fun onVariantClicked(variantOptions: VariantOptionWithAttribute, state: Int = IGNORE_STATE)
    fun getStockWording(): String = ""

    fun onVariantGuideLineClicked(url: String) {}
    fun onSelectionChanged(view: View, position: Int) {}
    fun onVariantGuideLineHide(): Boolean = false
    fun onVariantImageClicked(url: String) {}

    fun onQuantityUpdate(quantity: Int, productId: String, oldValue: Int) {}
    fun onDeleteQuantityClicked(productId: String) {}
    fun onClickRefresh() {}
    fun hideVariantName(): Boolean = false
    fun shouldHideTextHabis(): Boolean = false
}
