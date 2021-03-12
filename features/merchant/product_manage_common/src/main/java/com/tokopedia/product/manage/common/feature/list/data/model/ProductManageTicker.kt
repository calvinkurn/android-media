package com.tokopedia.product.manage.common.feature.list.data.model

import com.tokopedia.unifycomponents.ticker.Ticker

sealed class ProductManageTicker(val type: Int) {
    object MultiLocationTicker: ProductManageTicker(Ticker.TYPE_INFORMATION)
    object ManageStockNoAccessTicker: ProductManageTicker(Ticker.TYPE_INFORMATION)
    object EmptyStockTicker: ProductManageTicker(Ticker.TYPE_WARNING)
    object CampaignStockTicker: ProductManageTicker(Ticker.TYPE_INFORMATION)
    object NoTicker: ProductManageTicker(Ticker.TYPE_INFORMATION)

    fun shouldShow(): Boolean {
        return this != NoTicker
    }
}