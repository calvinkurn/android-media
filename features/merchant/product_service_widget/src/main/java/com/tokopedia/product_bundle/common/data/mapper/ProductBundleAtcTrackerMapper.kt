package com.tokopedia.product_bundle.common.data.mapper

import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.uimodel.AddToCartDataResult
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_bundle.multiple.presentation.model.ProductDetailBundleTracker

object ProductBundleAtcTrackerMapper {

    fun mapMultipleBundlingDataToProductDataTracker(
            productBundleDetails: List<ProductBundleDetail>,
            atcResult: AddToCartDataResult
    ): List<ProductDetailBundleTracker> {
        return productBundleDetails.map { productBundleDetail ->
            val productDetailWithCartId = atcResult.responseResult.data.find {
                it.productId == productBundleDetail.productId.toString()
            }

            ProductDetailBundleTracker(
                    productId = productBundleDetail.productId.toString(),
                    productName = productBundleDetail.productName,
                    productPrice = productBundleDetail.bundlePrice.toString(),
                    cartId = productDetailWithCartId?.cartId ?: "0",
                    quantity = productBundleDetail.productQuantity
            )
        }
    }

    fun mapSingleBundlingDataToProductTracker(
            bundleInfo: List<BundleInfo> = emptyList(),
            selectedBundleId: String,
            cartId: String
    ): List<ProductDetailBundleTracker> {
//        val productDetailWithCartId = atcResult.responseResult.data.find {
//            it.productId == selectedProductId
//        }
        val productDetails = bundleInfo.find {
            it.bundleID.toString() == selectedBundleId
        }

        if (productDetails != null) {
            return listOf(
                    ProductDetailBundleTracker(
                            productId = productDetails.bundleItems[0].productID.toString(),
                            productName = productDetails.bundleItems[0].name,
                            productPrice = productDetails.bundleItems[0].bundlePrice.toString(),
                            cartId = cartId,
                            quantity = productDetails.bundleItems[0].minOrder
                    )
            )
        } else {
            return listOf(
                    ProductDetailBundleTracker()
            )
        }
    }

}