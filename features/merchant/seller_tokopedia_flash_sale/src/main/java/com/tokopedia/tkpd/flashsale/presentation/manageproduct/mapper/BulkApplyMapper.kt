package com.tokopedia.tkpd.flashsale.presentation.manageproduct.mapper

import android.content.Context
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.ProductBulkApplyResult
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.ProductBulkApplyUiModel
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ChildProduct
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup

object BulkApplyMapper {

    private fun mapResultToWarehouse(
        warehouses: List<Warehouse>,
        result: ProductBulkApplyResult
    ): List<Warehouse> =
        warehouses.filter { it.isToggleOn }.map {
            Warehouse(
                it.warehouseId,
                it.name,
                it.stock,
                it.price,
                DiscountSetup(
                    it.discountSetup.discount,
                    result.discountAmount,
                    result.stock.toLong()
                ),
                it.isDilayaniTokopedia,
                it.isToggleOn,
                it.isDisabled,
                it.disabledReason
            )
        }

    fun mapBulkResultToProduct(
        product: Product,
        result: ProductBulkApplyResult
    ): Product = Product(
        product.childProducts.map { cp ->
            ChildProduct(
                cp.disabledReason,
                cp.isDisabled,
                cp.isMultiwarehouse,
                cp.isToggleOn,
                cp.name,
                cp.picture,
                cp.price,
                cp.productId,
                cp.sku,
                cp.stock,
                cp.url,
                mapResultToWarehouse(cp.warehouses, result)
            )
        },
        product.isMultiWarehouse,
        product.isParentProduct,
        product.name,
        product.picture,
        product.price,
        product.productCriteria,
        product.productId,
        product.sku,
        product.stock,
        product.url,
        mapResultToWarehouse(product.warehouses, result)
    )

    fun mapProductToBulkParam(
        context: Context,
        product: Product
    ): ProductBulkApplyUiModel {
        return ProductBulkApplyUiModel(
            bottomSheetTitle = context.getString(R.string.bulk_apply_title),
            isShowTextFieldProductDiscountBottomMessage = true,
            textStock = context.getString(R.string.bulk_apply_text_stock),
            textStockDescription = context.getString(R.string.bulk_apply_text_stock_description,
                product.productCriteria.minCustomStock, product.productCriteria.maxCustomStock),
            minimumStock = product.productCriteria.minCustomStock,
            maximumStock = product.productCriteria.maxCustomStock,
            minimumDiscountPrice = product.productCriteria.minFinalPrice.toInt(),
            maximumDiscountPrice = product.productCriteria.maxFinalPrice.toInt(),
            minimumDiscountPercentage = product.productCriteria.minDiscount.toInt(),
            maximumDiscountPercentage = product.productCriteria.maxDiscount.toInt(),
        )
    }

}