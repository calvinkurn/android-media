package com.tokopedia.shopdiscount.manage_discount.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.ShopDiscountManageDiscountTypeFactory
import java.util.*

data class ShopDiscountSetupProductUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val listSetupProductData: List<SetupProductData> = listOf()
) {
    data class SetupProductData(
        val productId: String = "",
        val productName: String = "",
        val productImageUrl: String = "",
        val stock: String = "",
        val listProductWarehouse: List<ProductWarehouse> = listOf(),
        val slashPriceInfo: ProductSlashPriceInfo = ProductSlashPriceInfo(),
        val price: ProductPrice = ProductPrice(),
        val listProductVariant: List<SetupProductData> = listOf(),
        var productStatus: ProductStatus = ProductStatus(),
        var mappedResultData: MappedResultData = MappedResultData(),
    ) : Visitable<ShopDiscountManageDiscountTypeFactory> {

        data class MappedResultData(
            val minOriginalPrice: Int = 0,
            val maxOriginalPrice: Int = 0,
            val minDisplayedPrice: Int = 0,
            val maxDisplayedPrice: Int = 0,
            val minDiscountPercentage: Int = 0,
            val maxDiscountPercentage: Int = 0,
            val totalVariant: Int = 0,
            val totalDiscountedVariant: Int = 0,
            val totalLocation: Int = 0
        )

        data class ProductStatus(
            val isProductDiscounted: Boolean = false,
            val isVariant: Boolean = false,
            val isMultiLoc: Boolean = false,
            val isProductError: Boolean = false,
        )

        data class ProductWarehouse(
            val warehouseId: String = "",
            val warehouseName: String = "",
            val warehouseLocation: String = "",
            val warehouseStock: String = "",
            val maxOrder: String = "",
            val abusiveRule: Boolean = false,
            val avgSoldPrice: Int = 0,
            val cheapestPrice: Int = 0,
            var discountedPrice: Int = 0,
            var discountedPercentage: Int = 0,
            val minRecommendationPrice: Int = 0,
            val minRecommendationPercentage: Int = 0,
            val maxRecommendationPrice: Int = 0,
            val maxRecommendationPercentage: Int = 0,
            val disable: Boolean = false,
            val disableRecommendation: Boolean = true,
            val warehouseType: Int = 0,
            val originalPrice: Int = 0
        )

        data class ProductSlashPriceInfo(
            val slashPriceProductId: String = "",
            val discountedPrice: Int = 0,
            val discountPercentage: Int = 0,
            var startDate: Date = Date(),
            var endDate: Date = Date(),
            val slashPriceStatusId: String = ""
        )

        data class ProductPrice(
            val min: Int = 0,
            val minFormatted: String = "",
            val max: Int = 0,
            val maxFormatted: String = ""
        )

        override fun type(typeFactory: ShopDiscountManageDiscountTypeFactory): Int {
            return typeFactory.type(this)
        }

    }

}