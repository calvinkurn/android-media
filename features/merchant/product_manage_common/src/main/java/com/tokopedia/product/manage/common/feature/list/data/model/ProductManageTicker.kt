package com.tokopedia.product.manage.common.feature.list.data.model

sealed class ProductManageTicker {
    object MultiLocationTicker: ProductManageTicker()
    object SingleLocationNoAccessTicker: ProductManageTicker()
    object MultiLocationNoAccessTicker: ProductManageTicker()
    object EmptyStockTicker: ProductManageTicker()
    object CampaignStockTicker: ProductManageTicker()
    object NoTicker: ProductManageTicker()

    fun shouldShowTicker(): Boolean {
        return this != NoTicker
    }
}