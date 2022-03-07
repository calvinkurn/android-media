package com.tokopedia.product.manage.feature.campaignstock.ui.util

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationDetailReserve
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationDetailSellable
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationReservedProduct
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedStockProductModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.mapVariantCampaignTypeToProduct
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

object CampaignStockMapper {

    fun mapToParcellableSellableProduct(sellableList: List<GetStockAllocationDetailSellable>,
                                        productVariantList: List<ProductVariant>): ArrayList<SellableStockProductUIModel> {
        val sellableSequence = sellableList.asSequence()
        val productVariantSequence = productVariantList.asSequence().apply {
            sortedWith(compareBy {
                it.id.toLongOrZero()
            })
        }
        val isAllStockEmpty = sellableList.all { it.stock.toIntOrZero() == 0 }
        val sellableProducts = sellableSequence
            .filter { sellable ->
                productVariantSequence.any { product -> product.id == sellable.productId }
            }
            .sortedWith(compareBy {
                it.productId.toLongOrZero()
            })
            .zip(productVariantSequence) { sellable, variant ->
                SellableStockProductUIModel(
                    productId = sellable.productId,
                    productName = sellable.productName,
                    stock = sellable.stock,
                    isActive = variant.status == ProductStatus.ACTIVE,
                    isAllStockEmpty = isAllStockEmpty,
                    access = variant.access,
                    isCampaign = variant.isCampaign,
                    campaignTypeList = mapVariantCampaignTypeToProduct(
                        variant.campaignTypeList
                    )
                )
            }.toList()
        return ArrayList(sellableProducts)
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

    fun getSellableProduct(id: Long,
                           isActive: Boolean,
                           access: ProductManageAccess,
                           isCampaign: Boolean,
                           sellableList: List<GetStockAllocationDetailSellable>): List<SellableStockProductUIModel> {
        return sellableList
                .filter { it.productId == id.toString() }
                .map { sellable ->
                    SellableStockProductUIModel(
                            productId = sellable.productId,
                            productName = sellable.productName,
                            stock = sellable.stock,
                            isActive = isActive,
                            isAllStockEmpty = sellable.stock.toIntOrZero() == 0,
                            access = access,
                            isCampaign = isCampaign,
                            campaignTypeList = mapVariantCampaignTypeToProduct(sellable.campaignTypeList)
                    )
                }
    }

    private fun mapToParcellableReservedProduct(product: GetStockAllocationReservedProduct): ReservedStockProductModel =
            with(product) {
                ReservedStockProductModel(productId, warehouseId, productName, description, stock)
            }

}

