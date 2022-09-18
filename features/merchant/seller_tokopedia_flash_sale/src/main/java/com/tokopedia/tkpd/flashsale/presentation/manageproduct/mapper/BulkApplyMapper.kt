package com.tokopedia.tkpd.flashsale.presentation.manageproduct.mapper

import android.content.Context
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.DiscountType
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.ProductBulkApplyResult
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.ProductBulkApplyUiModel
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ChildProduct
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.util.constant.NumericalNormalizationConstant.BULK_APPLY_PERCENT_NORMALIZATION
import com.tokopedia.tkpd.flashsale.util.constant.NumericalNormalizationConstant.BULK_APPLY_CURRENCY_NORMALIZATION
import kotlin.math.roundToInt

object BulkApplyMapper {

    private fun mapResultToWarehouse(
        warehouses: List<Warehouse>,
        result: ProductBulkApplyResult
    ): List<Warehouse> {
        return warehouses.filter { it.isToggleOn }.map {
            Warehouse(
                warehouseId = it.warehouseId,
                name = it.name,
                stock = it.stock,
                price = it.price,
                discountSetup = when (result.discountType) {
                    DiscountType.RUPIAH -> DiscountSetup(
                        discount = ((result.discountAmount.toDouble() / it.price) * BULK_APPLY_PERCENT_NORMALIZATION).roundToInt(),
                        price = result.discountAmount,
                        stock = result.stock.toLong()
                    )
                    DiscountType.PERCENTAGE -> DiscountSetup(
                        discount = result.discountAmount.toInt(),
                        price = ((result.discountAmount * BULK_APPLY_CURRENCY_NORMALIZATION) * it.price).toLong(),
                        stock = result.stock.toLong()
                    )
                },
                isDilayaniTokopedia = it.isDilayaniTokopedia,
                isToggleOn = it.isToggleOn,
                isDisabled = it.isDisabled,
                disabledReason = it.disabledReason
            )
        }
    }

    fun mapBulkResultToProduct(
        product: Product,
        result: ProductBulkApplyResult
    ): Product = Product(
        childProducts = product.childProducts.map { cp ->
            ChildProduct(
                disabledReason = cp.disabledReason,
                isDisabled = cp.isDisabled,
                isMultiwarehouse = cp.isMultiwarehouse,
                isToggleOn = cp.isToggleOn,
                name = cp.name,
                picture = cp.picture,
                price = cp.price,
                productId = cp.productId,
                sku = cp.sku,
                stock = cp.stock,
                url = cp.url,
                warehouses = mapResultToWarehouse(cp.warehouses, result)
            )
        },
        isMultiWarehouse = product.isMultiWarehouse,
        isParentProduct = product.isParentProduct,
        name = product.name,
        picture = product.picture,
        price = product.price,
        productCriteria = product.productCriteria,
        productId = product.productId,
        sku = product.sku,
        stock = product.stock,
        url = product.url,
        warehouses = mapResultToWarehouse(product.warehouses, result)
    )

    fun mapProductToBulkParam(
        context: Context,
        product: Product
    ): ProductBulkApplyUiModel {
        return ProductBulkApplyUiModel(
            bottomSheetTitle = context.getString(R.string.bulk_apply_title),
            isShowTextFieldProductDiscountBottomMessage = true,
            textStock = context.getString(R.string.bulk_apply_text_stock),
            textStockDescription = context.getString(
                R.string.bulk_apply_text_stock_description,
                product.productCriteria.minCustomStock, product.productCriteria.maxCustomStock
            ),
            minimumStock = product.productCriteria.minCustomStock,
            maximumStock = product.productCriteria.maxCustomStock,
            minimumDiscountPrice = product.productCriteria.minFinalPrice.toInt(),
            maximumDiscountPrice = product.productCriteria.maxFinalPrice.toInt(),
            minimumDiscountPercentage = product.productCriteria.minDiscount.toInt(),
            maximumDiscountPercentage = product.productCriteria.maxDiscount.toInt(),
        )
    }

}