package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

/**
 * Created by Yehezkiel on 17/05/21
 */
data class ProductVariantResult(
        var atcMessage: String = "",
        var selectedProductId: String = "",
        var parentProductId: String = "",
        var requestCode: Int = 0,

        /**
         * PDP Only
         */
        var shouldRefreshValidateOvo: Boolean = false,
        var listOfVariantSelected: List<VariantCategory>? = null,
        var mapOfSelectedVariantOption: MutableMap<String, String>? = null
) {
    fun onSuccessAtc():Boolean {
        return atcMessage.isNotEmpty()
    }
}