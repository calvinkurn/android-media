package com.tokopedia.product.detail.common.data.model.aggregator

/**
 * Created by Yehezkiel on 11/05/21
 */
data class ProductVariantBottomSheetParams(
        val productId: String = "",
        val pageSource: String = "",
        val productParentId: String = "",
        val isTokoNow: Boolean = false,
        val whId: String = "",

        /**
         * PDP only
         */
        val pdpSession: String = "",
        val variantAggregator: ProductVariantAggregator = ProductVariantAggregator()
)