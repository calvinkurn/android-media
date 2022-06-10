package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignProductListResponse
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import javax.inject.Inject

class SellerCampaignProductListMapper @Inject constructor() {
    fun map(data: GetSellerCampaignProductListResponse): SellerCampaignProductList {
        return SellerCampaignProductList(
            success = data.getSellerCampaignProductList.responseHeader.success,
            errorMessage = data.getSellerCampaignProductList.responseHeader.errorMessage,
            productList = data.getSellerCampaignProductList.productList.map { product ->
               SellerCampaignProductList.ProductList(
                   productId = product.productId,
                   parentId = product.parentId,
                   productName = product.productName,
                   productUrl = product.productUrl,
                   productSku = product.productSku,
                   price = product.price,
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
                       originalStock = product.productMapData.originalStock,
                       campaignSoldCount = product.productMapData.campaignSoldCount,
                       maxOrder = product.productMapData.maxOrder
                   ),
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
                   highlightProductWording = product.highlightProductWording
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
}