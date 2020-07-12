package com.tokopedia.product.manage.feature.campaignstock.domain

import com.tokopedia.product.manage.feature.campaignstock.domain.model.*

object DummySource {

    fun getCampaignStockVariant(): GetStockAllocationData {
        return GetStockAllocationData(
                summary = GetStockAllocationSummary(
                        isVariant = false,
                        productName = "Test name",
                        sellableStock = "60",
                        reserveStock = "20",
                        totalStock = "80"
                ),
                detail = GetStockAllocationDetail(
                        sellable = listOf(
                                GetStockAllocationDetailSellable(
                                        productId = "12345",
                                        warehouseId = "",
                                        productName = "Test sellable",
                                        stock = "60"
                                )
                        ),
                        reserve = listOf(
                                GetStockAllocationDetailReserve(
                                        eventInfo = GetStockAllocationEventInfo(
                                                eventType = "Pesta Diskon",
                                                eventName = "Diskon 40% | 50rb",
                                                description = "Murah lho belanja",
                                                stock = "10",
                                                actionWording = "Lihat detail",
                                                product = listOf(
                                                        GetStockAllocationReservedProduct(
                                                                productName = "TEst reserve 1",
                                                                description = "Ini untuk tes aja",
                                                                stock = "5"
                                                        ),
                                                        GetStockAllocationReservedProduct(
                                                                productName = "TEst reserve 2",
                                                                description = "Ini untuk tes aja",
                                                                stock = "5"
                                                        )
                                                )
                                        )
                                ),
                                GetStockAllocationDetailReserve(
                                        eventInfo = GetStockAllocationEventInfo(
                                                eventType = "Pesta Diskon",
                                                eventName = "Diskon 40% | 50rb",
                                                description = "Murah lho belanja",
                                                stock = "10",
                                                actionWording = "Lihat detail",
                                                product = listOf(
                                                        GetStockAllocationReservedProduct(
                                                                productName = "TEst reserve 1",
                                                                description = "Ini untuk tes aja",
                                                                stock = "5"
                                                        ),
                                                        GetStockAllocationReservedProduct(
                                                                productName = "TEst reserve 2",
                                                                description = "Ini untuk tes aja",
                                                                stock = "5"
                                                        )
                                                )
                                        )
                                ),
                                GetStockAllocationDetailReserve(
                                        eventInfo = GetStockAllocationEventInfo(
                                                eventType = "Pesta Diskon",
                                                eventName = "Diskon 40% | 50rb",
                                                description = "Murah lho belanja",
                                                stock = "10",
                                                actionWording = "Lihat detail",
                                                product = listOf(
                                                        GetStockAllocationReservedProduct(
                                                                productName = "TEst reserve 1",
                                                                description = "Ini untuk tes aja",
                                                                stock = "5"
                                                        ),
                                                        GetStockAllocationReservedProduct(
                                                                productName = "TEst reserve 2",
                                                                description = "Ini untuk tes aja",
                                                                stock = "5"
                                                        )
                                                )
                                        )
                                )
                        )
                )
        )
    }
}