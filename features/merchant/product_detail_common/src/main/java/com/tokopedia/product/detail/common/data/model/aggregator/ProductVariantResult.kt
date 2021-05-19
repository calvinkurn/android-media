package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

/**
 * Created by Yehezkiel on 17/05/21
 */
data class ProductVariantResult(
        var successAtc: Boolean = false,
        var atcMessage: String = "",
        var productId: String = "",
        var listOfVariantSelected: List<VariantCategory>? = null,
        var mapOfSelectedVariantOption: MutableMap<String, String>? = null
)