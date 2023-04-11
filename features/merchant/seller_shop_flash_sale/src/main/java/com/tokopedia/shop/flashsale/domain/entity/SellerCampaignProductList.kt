package com.tokopedia.shop.flashsale.domain.entity

import android.os.Parcelable
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorType
import kotlinx.parcelize.Parcelize

data class SellerCampaignProductList(
    val success : Boolean = false,
    val errorMessage : List<String> = listOf(),
    val productList: List<Product> = listOf(),
    val totalProduct: Int = 0,
    val totalProductSold: Int = 0,
    val countAcceptedProduct: Int = 0,
    val totalProductQty: Int = 0,
    val totalIncome: Int = 0,
    val totalIncomeFormatted: String = "",
    val productFailedCount: Int = 0
) {
    @Parcelize
    data class Product(
        val productId: String = "",
        val parentId: String = "",
        val productName: String = "",
        val productUrl: String = "",
        val productSku: String = "",
        val price: Int = 0,
        val formattedPrice: String = "",
        val imageUrl: ImageUrl = ImageUrl(),
        val productMapData: ProductMapData = ProductMapData(),
        val warehouseList: List<WarehouseData> = listOf(),
        val viewCount: Int = 0,
        val highlightProductWording: String = "",
        var isInfoComplete: Boolean = false,
        var errorType: ManageProductErrorType = ManageProductErrorType.NOT_ERROR
    ): Parcelable

    @Parcelize
    data class ImageUrl(
        val img100Square: String = "",
        val img200: String = "",
        val img300: String = "",
        val img700: String = "",
    ): Parcelable

    @Parcelize
    data class ProductMapData(
        val productMapId: String = "",
        val campaignId: String = "",
        val productMapStatus: Int = 0,
        val productMapAdminStatus: Int = 0,
        val originalPrice: Long = 0,
        val discountedPrice: Long = 0,
        val discountPercentage: Int = 0,
        val customStock: Long = 0,
        val originalCustomStock: Int = 0,
        val originalStock: Int = 0,
        val campaignSoldCount: Int = 0,
        val maxOrder: Int = 0
    ): Parcelable

    @Parcelize
    data class WarehouseData(
        val warehouseId: String = "",
        val warehouseName: String = "",
        val stock: Int = 0,
        val chosenWarehouse: Boolean = false,
        val originalCustomStock: Int = 0,
        val customStock: Int = 0,
    ): Parcelable
}
