package com.tokopedia.product.detail.view.listener

import com.tokopedia.product.detail.data.model.variant.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 02/03/20
 */
interface ProductVariantListener {
    fun onVariantClicked(variantOptions: VariantOptionWithAttribute)
    fun onVariantGuideLineClicked(url: String)
    fun getStockWording(): String
}