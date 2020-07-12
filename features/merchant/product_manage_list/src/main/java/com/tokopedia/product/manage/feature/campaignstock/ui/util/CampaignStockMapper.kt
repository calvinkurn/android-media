package com.tokopedia.product.manage.feature.campaignstock.ui.util

import com.tokopedia.product.manage.feature.campaignstock.domain.model.CampaignStockVariantProduct
import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationDetailReserve
import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationDetailSellable
import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationReservedProduct
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ReservedStockProductModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.SellableStockProductUIModel

object CampaignStockMapper {

    const val ACTIVE = "ACTIVE"

    fun mapToParcellableSellableProduct(sellable: GetStockAllocationDetailSellable,
                                        variantList: List<CampaignStockVariantProduct>): SellableStockProductUIModel =
            with(sellable) {
                SellableStockProductUIModel(
                        productId = productId,
                        warehouseId = warehouseId,
                        productName = productName,
                        stock = stock,
                        isActive = productId.mapToIsActive(variantList)
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

    private fun String.mapToIsActive(variantList: List<CampaignStockVariantProduct>): Boolean =
            variantList.firstOrNull { it.productId == this }?.status == ACTIVE
}

