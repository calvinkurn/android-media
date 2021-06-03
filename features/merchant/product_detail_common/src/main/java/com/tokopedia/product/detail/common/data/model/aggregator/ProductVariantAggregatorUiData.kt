package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo

/**
 * Created by Yehezkiel on 18/05/21
 */
data class ProductVariantAggregatorUiData (
        var variantData: ProductVariant = ProductVariant(),

        val cardRedirection: Map<String,CartTypeData> = mapOf(),

        var nearestWarehouse: Map<String,WarehouseInfo> = mapOf()
) {
    fun isAggregatorEmpty(): Boolean {
        return (!variantData.hasChildren && !variantData.hasVariant) || cardRedirection.isEmpty() || nearestWarehouse.isEmpty()
    }
}
