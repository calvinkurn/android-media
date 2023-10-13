package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

data class ProductDetailDataModel(
    val layoutData: DynamicProductInfoP1 = DynamicProductInfoP1(),
    val listOfLayout: MutableList<DynamicPdpDataModel> = mutableListOf(),
    val variantData: ProductVariant? = null,
    val cacheState: CacheState = CacheState(),
    val isCampaign: Boolean = false
) {

    val isFromCache
        get() = cacheState.isFromCache
}

data class CacheState(
    val remoteCacheableActive: Boolean = false,
    // data source state, true = from cache, false = from cloud
    val isFromCache: Boolean = false,

    // caching flow pdp, true = cache first then cloud, false = cache only or cloud only
    val cacheFirstThenCloud: Boolean = false
)
