package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleSubmittedProductListResponse
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProductData
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
                submittedProduct.mainStock,
                submittedProduct.name,
                submittedProduct.picture,
                submittedProduct.toProductCriteria(),
                submittedProduct.productId,
                submittedProduct.url,
                submittedProduct.toWarehouses()
            )
        }
    }

    private fun GetFlashSaleSubmittedProductListResponse.Product.toProductCriteria(): SubmittedProduct.ProductCriteria {
        return SubmittedProduct.ProductCriteria(
            productCriteria.criteriaId
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
                warehouse.warehouseId
            )
        }
    }
}