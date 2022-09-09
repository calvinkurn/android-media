package com.tokopedia.shopdiscount.manage_product_discount.util

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_product_discount.data.uimodel.ShopDiscountManageProductVariantItemUiModel

object ShopDiscountManageProductVariantMapper {

    fun mapToListShopDiscountManageProductVariantItemUiModel(
        productData: ShopDiscountSetupProductUiModel.SetupProductData
    ): List<ShopDiscountManageProductVariantItemUiModel> {
        return productData.listProductVariant.map {
            ShopDiscountManageProductVariantItemUiModel(
                variantId = it.productId,
                variantName = it.productName,
                variantMinOriginalPrice = getVariantOriginalPrice(it).minOrNull().orZero(),
                variantMaxOriginalPrice = getVariantOriginalPrice(it).maxOrNull().orZero(),
                variantStock = it.stock.toIntOrZero(),
                variantTotalLocation = it.listProductWarehouse.size,
                isEnabled = it.variantStatus.isVariantEnabled.orFalse(),
                startDate = it.slashPriceInfo.startDate,
                endDate = it.slashPriceInfo.endDate,
                discountedPrice = it.slashPriceInfo.discountedPrice,
                discountedPercentage = it.slashPriceInfo.discountPercentage,
                maxOrder = it.listProductWarehouse.firstOrNull()?.maxOrder.orEmpty(),
                isMultiLoc = it.variantStatus.isMultiLoc,
                slashPriceStatusId = it.slashPriceInfo.slashPriceStatusId,
                isAbusive = it.listProductWarehouse.any { it.abusiveRule },
                averageSoldPrice = it.listProductWarehouse.firstOrNull()?.avgSoldPrice.orZero(),
                productErrorType = productData.productStatus.errorType
            )
        }
    }

    private fun getVariantOriginalPrice(
        productData: ShopDiscountSetupProductUiModel.SetupProductData
    ): List<Int> {
        return productData.getListEnabledProductWarehouse().map {
            it.originalPrice
        }
    }

}
