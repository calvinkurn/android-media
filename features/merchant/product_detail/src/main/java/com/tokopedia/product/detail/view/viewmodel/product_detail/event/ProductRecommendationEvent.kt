package com.tokopedia.product.detail.view.viewmodel.product_detail.event

import com.tokopedia.minicart.common.domain.data.MiniCartItem

sealed class ProductRecommendationEvent {
    data class LoadRecommendation(
        val pageName: String,
        val productId: String,
        val isTokoNow: Boolean,
        val miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>?,
        val queryParam: String,
        val thematicId: String
    ) : ProductRecommendationEvent()
}
