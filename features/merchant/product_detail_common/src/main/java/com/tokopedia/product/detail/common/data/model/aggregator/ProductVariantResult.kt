package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

/**
 * Created by Yehezkiel on 17/05/21
 */
data class ProductVariantResult(
        val successAtc: Boolean = false,
        val atcMessage: String = "",
        val listOfVariantSelected: List<VariantCategory>? = null,
        val mapOfSelectedVariantOption: MutableMap<String, String>? = null
)