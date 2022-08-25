package com.tokopedia.product_bundle.common.data.mapper

import com.tokopedia.product_bundle.common.data.model.uimodel.AddToCartDataResult
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_bundle.multiple.presentation.model.ProductDetailMultipleBundleTracker

object ProductBundleAtcTrackerMapper {

    fun mapMultipleBundlingDataToProductDataTracker(
            productBundleDetails: List<ProductBundleDetail>,
            atcResult: AddToCartDataResult
    ): List<ProductDetailMultipleBundleTracker> {
        return productBundleDetails.map { productBundleDetail ->
            val productDetailWithCartId = atcResult.responseResult.data.find {
                it.productId == productBundleDetail.productId.toString()
            }

            ProductDetailMultipleBundleTracker(
                    productId = productBundleDetail.productId.toString(),
                    productName = productBundleDetail.productName,
                    productPrice = productBundleDetail.bundlePrice.toString(),
                    cartId = productDetailWithCartId?.cartId ?: "0",
                    quantity = productBundleDetail.productQuantity
            )
        }
    }

}