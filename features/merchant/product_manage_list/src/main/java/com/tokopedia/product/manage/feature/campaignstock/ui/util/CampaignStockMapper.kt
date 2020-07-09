package com.tokopedia.product.manage.feature.campaignstock.ui.util

import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationDetailReserve
import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationDetailSellable
import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationReservedProduct
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ReservedStockProductModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.SellableStockProductUIModel

object CampaignStockMapper {

    fun mapToParcellableSellableProduct(dataModel: GetStockAllocationDetailSellable): SellableStockProductUIModel =
            with(dataModel) {
                SellableStockProductUIModel(
                        productId = productId,
                        warehouseId = warehouseId,
                        productName = productName,
                        stock = stock
                )
            }

    fun mapToParcellableReserved(dataModel: GetStockAllocationDetailReserve): ReservedEventInfoUiModel =
            with(dataModel.eventInfo) {
                ReservedEventInfoUiModel(
                        eventType = eventType,
                        eventName = eventName,
                        eventDesc = description,
                        stock = stock,
                        actionWording = actionWording,
                        actionUrl = actionUrl,
                        products = product.map { mapToParcellableReservedProduct(it) } as ArrayList<ReservedStockProductModel>
                )
            }

    private fun mapToParcellableReservedProduct(product: GetStockAllocationReservedProduct): ReservedStockProductModel =
            with(product) {
                ReservedStockProductModel(productId, warehouseId, productName, description, stock)
            }

}

