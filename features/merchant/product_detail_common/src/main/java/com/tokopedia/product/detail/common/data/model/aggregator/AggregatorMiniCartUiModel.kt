package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.minicart.common.domain.data.MiniCartItem

/**
 * Created by Yehezkiel on 24/05/21
 */
data class AggregatorMiniCartUiModel(
        val variantAggregator: ProductVariantAggregatorUiData = ProductVariantAggregatorUiData(),
        val miniCartData: Map<String, MiniCartItem>? = null
)