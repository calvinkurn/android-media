package com.tokopedia.shop.flashsale.presentation.creation.manage.mapper

import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel

object WarehouseUiModelMapper {
    fun map(result: List<SellerCampaignProductList.WarehouseData>): List<WarehouseUiModel> = result.map {
        WarehouseUiModel(
            id = it.warehouseId,
            name = it.warehouseName,
            stock = it.stock.toLong(),
            isSelected = it.chosenWarehouse
        )
    }
}