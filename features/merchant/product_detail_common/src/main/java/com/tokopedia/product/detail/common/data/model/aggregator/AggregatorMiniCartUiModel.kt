package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.minicart.common.domain.data.MiniCartItem2
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey

/**
 * Created by Yehezkiel on 24/05/21
 */
data class AggregatorMiniCartUiModel(
        val variantAggregator: ProductVariantAggregatorUiData = ProductVariantAggregatorUiData(),
        val miniCartData: Map<MiniCartItemKey, MiniCartItem2>? = null
)