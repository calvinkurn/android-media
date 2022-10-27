package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleSubmittedProductListResponse
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProductData
import com.tokopedia.tkpd.flashsale.domain.entity.enums.ProductStockStatus
import javax.inject.Inject

class GetFlashSaleSubmittedProductListMapper @Inject constructor() {

    fun map(data: GetFlashSaleSubmittedProductListResponse): SubmittedProductData {
        return SubmittedProductData(
            data.toSubmittedProduct(),
            data.getFlashSaleSubmittedProductList.data.totalProduct
        )
    }

    private fun GetFlashSaleSubmittedProductListResponse.toSubmittedProduct(): List<SubmittedProduct> {
        return getFlashSaleSubmittedProductList.data.productList.map { submittedProduct ->
            SubmittedProduct(
                submittedProduct.campaignStock,
                submittedProduct.isMultiwarehouse,
                submittedProduct.isParentProduct,
                submittedProduct.totalChild,
                submittedProduct.soldCount,
                submittedProduct.mainStock,
                submittedProduct.name,
                submittedProduct.picture,
                submittedProduct.toProductCriteria(),
                submittedProduct.productId,
                submittedProduct.url,
                submittedProduct.toPrice(),
                submittedProduct.toDiscount(),
                submittedProduct.toDiscountedPrice(),
                submittedProduct.toProductStockStatus(),
                submittedProduct.toWarehouses(),
                submittedProduct.totalSubsidy,
                submittedProduct.statusText,
                submittedProduct.countLocation
            )
        }
    }

    private fun GetFlashSaleSubmittedProductListResponse.Product.toPrice(): SubmittedProduct.Price {
        return SubmittedProduct.Price(
            price.price.orZero(),
            price.lowerPrice.orZero(),
            price.upperPrice.orZero()
        )
    }

    private fun GetFlashSaleSubmittedProductListResponse.Product.toDiscount(): SubmittedProduct.Discount {
        return SubmittedProduct.Discount(
            discount.discount.orZero(),
            discount.lowerDiscount.orZero(),
            discount.upperDiscount.orZero()
        )
    }

    private fun GetFlashSaleSubmittedProductListResponse.Product.toDiscountedPrice(): SubmittedProduct.DiscountedPrice {
        return SubmittedProduct.DiscountedPrice(
            discountedPrice.price.orZero(),
            discountedPrice.lowerPrice.orZero(),
            discountedPrice.upperPrice.orZero()
        )
    }

    private fun GetFlashSaleSubmittedProductListResponse.Product.toWarehouses(): List<SubmittedProduct.Warehouse> {
        return warehouses.map { warehouse ->
            SubmittedProduct.Warehouse(
                warehouse.name,
                warehouse.price,
                warehouse.rejectionReason,
                warehouse.statusId,
                warehouse.statusText,
                warehouse.stock,
                warehouse.toDiscountSetup(),
                warehouse.toSubsidy(),
                warehouse.warehouseId,
                warehouse.soldCount
            )
        }
    }

    private fun GetFlashSaleSubmittedProductListResponse.Warehouse.toDiscountSetup(): SubmittedProduct.DiscountSetup {
        return SubmittedProduct.DiscountSetup(
            discountSetup?.price.orZero(),
            discountSetup?.stock.orZero(),
            discountSetup?.discount.orZero()
        )
    }

    private fun GetFlashSaleSubmittedProductListResponse.Warehouse.toSubsidy(): SubmittedProduct.Subsidy {
        return SubmittedProduct.Subsidy(
            subsidy?.hasSubsidy ?: false,
            subsidy?.subsidyAmount.orZero()
        )
    }

    private fun GetFlashSaleSubmittedProductListResponse.Product.toProductCriteria(): SubmittedProduct.ProductCriteria {
        return SubmittedProduct.ProductCriteria(
            productCriteria.criteriaId
        )
    }

    private fun GetFlashSaleSubmittedProductListResponse.Product.toProductStockStatus(): ProductStockStatus {
        return if (isParentProduct) {
            if (countLocation > Int.ONE) {
                ProductStockStatus.MULTI_VARIANT_MULTI_LOCATION
            } else {
                ProductStockStatus.MULTI_VARIANT_SINGLE_LOCATION
            }
        } else {
            if (countLocation > Int.ONE) {
                ProductStockStatus.SINGLE_VARIANT_MULTI_LOCATION
            } else {
                ProductStockStatus.SINGLE_VARIANT_SINGLE_LOCATION
            }
        }
    }
}
