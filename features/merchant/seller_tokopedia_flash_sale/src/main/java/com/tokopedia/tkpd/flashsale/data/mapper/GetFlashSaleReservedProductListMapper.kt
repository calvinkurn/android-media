package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleReservedProductListResponse
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import javax.inject.Inject

class GetFlashSaleReservedProductListMapper @Inject constructor() {

    fun map(response: GetFlashSaleReservedProductListResponse): ReservedProduct {
        val products = response.getFlashSaleReservedProductList.toProduct()
        return ReservedProduct(products, response.getFlashSaleReservedProductList.totalProduct)
    }

    private fun GetFlashSaleReservedProductListResponse.GetFlashSaleReservedProductList.toProduct(): List<ReservedProduct.Product> {
        return this.productList.map { product ->
            ReservedProduct.Product(
                product.toChildProduct(),
                product.isMultiWarehouse,
                product.isParentProduct,
                product.name,
                product.picture,
                product.toPrice(),
                product.toProductCriteria(),
                product.productId,
                product.sku,
                product.stock,
                product.url,
                product.toWarehouses()
            )
        }
    }

    private fun GetFlashSaleReservedProductListResponse.GetFlashSaleReservedProductList.Product.toChildProduct(): List<ReservedProduct.Product.ChildProduct> {
        return childProducts.map { childProduct ->
            val childWarehouses = childProduct.warehouses.map { warehouse ->
                ReservedProduct.Product.Warehouse(
                    warehouse.warehouseId.toLongOrZero(),
                    warehouse.name,
                    warehouse.stock,
                    warehouse.price.toLongOrZero(),
                    ReservedProduct.Product.Warehouse.DiscountSetup(
                        warehouse.discountSetup.discount,
                        warehouse.discountSetup.price.toLongOrZero(),
                        warehouse.discountSetup.stock
                    ),
                    warehouse.isDilayaniTokopedia,
                    isToggleOn = !warehouse.discountSetup.stock.isZero(),
                    warehouse.isDisabled,
                    warehouse.disabledReason
                )
            }
            ReservedProduct.Product.ChildProduct(
                childProduct.disabledReason,
                childProduct.isDisabled,
                childProduct.isMultiwarehouse,
                isToggleOn = childWarehouses.any { it.isToggleOn },
                childProduct.name,
                childProduct.picture,
                ReservedProduct.Product.Price(
                    childProduct.price.lowerPrice.toLongOrZero(),
                    childProduct.price.price.toLongOrZero(),
                    childProduct.price.upperPrice.toLongOrZero()
                ),
                ReservedProduct.Product.ProductCriteria(
                    productCriteria.criteriaId.toLongOrZero(),
                    productCriteria.maxCustomStock,
                    productCriteria.maxDiscount,
                    productCriteria.maxFinalPrice.toLongOrZero(),
                    productCriteria.minCustomStock,
                    productCriteria.minDiscount,
                    productCriteria.minFinalPrice.toLongOrZero()
                ),
                0,
                0,
                childProduct.productId,
                childProduct.sku,
                childProduct.stock,
                childProduct.url,
                childWarehouses
            )
        }
    }

    private fun GetFlashSaleReservedProductListResponse.GetFlashSaleReservedProductList.Product.toPrice(): ReservedProduct.Product.Price {
        return ReservedProduct.Product.Price(
            price.lowerPrice.toLongOrZero(),
            price.price.toLongOrZero(),
            price.upperPrice.toLongOrZero()
        )
    }

    private fun GetFlashSaleReservedProductListResponse.GetFlashSaleReservedProductList.Product.toProductCriteria(): ReservedProduct.Product.ProductCriteria {
        return ReservedProduct.Product.ProductCriteria(
            productCriteria.criteriaId.toLongOrZero(),
            productCriteria.maxCustomStock,
            productCriteria.maxDiscount,
            productCriteria.maxFinalPrice.toLongOrZero(),
            productCriteria.minCustomStock,
            productCriteria.minDiscount,
            productCriteria.minFinalPrice.toLongOrZero()
        )
    }

    private fun GetFlashSaleReservedProductListResponse.GetFlashSaleReservedProductList.Product.toWarehouses(): List<ReservedProduct.Product.Warehouse> {
        return warehouses.map { warehouse ->
            ReservedProduct.Product.Warehouse(
                warehouse.warehouseId.toLongOrZero(),
                warehouse.name,
                warehouse.stock,
                warehouse.price.toLongOrZero(),
                ReservedProduct.Product.Warehouse.DiscountSetup(
                    warehouse.discountSetup.discount,
                    warehouse.discountSetup.price.toLongOrZero(),
                    warehouse.discountSetup.stock
                ),
                warehouse.isDilayaniTokopedia,
                isToggleOn = !warehouse.discountSetup.stock.isZero(),
                warehouse.isDisabled,
                warehouse.disabledReason
            )
        }
    }
}
