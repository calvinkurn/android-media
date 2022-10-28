package com.tokopedia.shop.flashsale.presentation.creation.manage.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.extension.isZero
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel
import javax.inject.Inject

class WarehouseUiModelMapper @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val MULTILOC_MIN_SHOP_COUNT = 2
        private const val WAREHOUSE_NAME_DEFAULT_SERVER = "Shop Location"
    }

    /**
     * when selected warehouse's stock is out of stock,
     * then search for new warehouse with largest stock for new selection
     */
    private fun List<WarehouseUiModel>.fixWarehouseSelection(): List<WarehouseUiModel> {
        val isSelectedWarehouseOutOfStock = any { it.isSelected && it.stock.isZero() }
        if (isSelectedWarehouseOutOfStock) {
            val whId = maxByOrNull { it.stock }?.id
            forEach {
                it.isSelected = it.id == whId
            }
        }
        return this
    }

    fun getString(resId: Int): String? {
        return try {
            context.getString(resId)
        } catch (e: Exception) {
            null
        }
    }

    fun map(result: List<SellerCampaignProductList.WarehouseData>): List<WarehouseUiModel> =
        result.map {
            val warehouseName = if (it.warehouseName == WAREHOUSE_NAME_DEFAULT_SERVER) {
                getString(R.string.editproduct_warehouse_title_default) ?: it.warehouseName
            } else it.warehouseName

            WarehouseUiModel(
                id = it.warehouseId,
                name = warehouseName,
                stock = it.stock.toLong(),
                isSelected = it.chosenWarehouse
            )
        }.fixWarehouseSelection()

    fun isShopMultiloc(it: List<WarehouseUiModel>): Boolean {
        return it.size >= MULTILOC_MIN_SHOP_COUNT
    }
}
