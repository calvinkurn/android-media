package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.shop.flashsale.common.util.ProductErrorStatusHandler
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignProductListResponse
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorType
import javax.inject.Inject

class SellerCampaignProductListMapper @Inject constructor(private val productErrorStatusHandler: ProductErrorStatusHandler) {
    fun map(data: GetSellerCampaignProductListResponse): SellerCampaignProductList {
        return SellerCampaignProductList(
            success = data.getSellerCampaignProductList.responseHeader.success,
            errorMessage = data.getSellerCampaignProductList.responseHeader.errorMessage,
            productList = data.getSellerCampaignProductList.productList.map { product ->
               SellerCampaignProductList.Product(
                   productId = product.productId,
                   parentId = product.parentId,
                   productName = product.productName,
                   productUrl = product.productUrl,
                   productSku = product.productSku,
                   price = product.price.toInt(),
                   formattedPrice = product.formattedPrice,
                   imageUrl = SellerCampaignProductList.ImageUrl(
                       img100Square = product.imageUrl.img100Square,
                       img200 = product.imageUrl.img200,
                       img300 = product.imageUrl.img300,
                       img700 = product.imageUrl.img700
                   ),
                   productMapData = SellerCampaignProductList.ProductMapData(
                       productMapId = product.productMapData.productMapId,
                       campaignId = product.productMapData.campaignId,
                       productMapStatus = product.productMapData.productMapStatus,
                       productMapAdminStatus = product.productMapData.productMapAdminStatus,
                       originalPrice = product.productMapData.originalPrice,
                       discountedPrice = product.productMapData.discountedPrice,
                       discountPercentage = product.productMapData.discountPercentage,
                       customStock = product.productMapData.customStock,
                       originalCustomStock = product.productMapData.originalCustomStock,
                       originalStock = product.stock,
                       campaignSoldCount = product.productMapData.campaignSoldCount,
                       maxOrder = product.productMapData.maxOrder
                   ),
                   isInfoComplete = product.toProductInfoCompletion(),
                   warehouseList = product.warehouseList.map { warehouse ->
                       SellerCampaignProductList.WarehouseData(
                           warehouseId = warehouse.warehouseId,
                           warehouseName = warehouse.warehouseName,
                           stock = warehouse.stock,
                           chosenWarehouse = warehouse.chosenWarehouse,
                           originalCustomStock = warehouse.originalCustomStock,
                           customStock = warehouse.customStock
                       )
                   },
                   viewCount = product.viewCount,
                   highlightProductWording = product.highlightProductWording,
                   errorType = product.toErrorType()
               )
            },
            totalProduct = data.getSellerCampaignProductList.totalProduct,
            totalProductSold = data.getSellerCampaignProductList.totalProductSold,
            countAcceptedProduct = data.getSellerCampaignProductList.countAcceptedProduct,
            totalProductQty = data.getSellerCampaignProductList.totalProductQty,
            totalIncome = data.getSellerCampaignProductList.totalIncome,
            totalIncomeFormatted = data.getSellerCampaignProductList.totalIncomeFormatted,
            productFailedCount = data.getSellerCampaignProductList.productFailedCount
        )
    }

    private fun GetSellerCampaignProductListResponse.ProductList.toProductInfoCompletion(): Boolean {
        return when {
            this.productMapData.discountedPrice.isZero() -> false
            this.productMapData.discountPercentage.isZero() -> false
            this.productMapData.originalCustomStock.isZero() -> false
            this.productMapData.customStock.isZero() -> false
            this.productMapData.maxOrder.isZero() -> false
            else -> true
        }
    }

    private fun GetSellerCampaignProductListResponse.ProductList.toErrorType(): ManageProductErrorType {
        return productErrorStatusHandler.getErrorType(this.productMapData)
    }
}
