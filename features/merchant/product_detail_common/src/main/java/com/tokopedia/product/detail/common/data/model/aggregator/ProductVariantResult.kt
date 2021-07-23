package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

/**
 * Created by Yehezkiel on 17/05/21
 */
data class ProductVariantResult(
        /**
         * Global data
         * selectedProductId : if user change variant in the bottom sheet, it will update with selected product id from child (if partially select, it will not update)
         * requestCode : for now, this only for request code cart checkout, and usually update cart counter in previous page
         */
        var atcMessage: String = "",
        var selectedProductId: String = "",
        var parentProductId: String = "",
        var requestCode: Int = 0,

        /**
         * PDP Only
         * shouldRefreshPreviousPage : will be true if validate ovo give true or after click ingatkan saya
         */
        var shouldRefreshPreviousPage: Boolean = false,
        var listOfVariantSelected: List<VariantCategory>? = null,
        var mapOfSelectedVariantOption: MutableMap<String, String>? = null
)