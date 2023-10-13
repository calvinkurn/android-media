package com.tokopedia.atc_common.domain.mapper

import com.tokopedia.atc_common.data.model.response.AddToCartBundleDataResponse
import com.tokopedia.atc_common.data.model.response.AddToCartBundleResponse
import com.tokopedia.atc_common.data.model.response.ProductDataResponse
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleModel
import com.tokopedia.atc_common.domain.model.response.ProductDataModel
import javax.inject.Inject

class AddToCartBundleDataMapper @Inject constructor() {

    fun mapAddToCartBundleResponse(response: AddToCartBundleResponse): AddToCartBundleModel {
        return AddToCartBundleModel().apply {
            status = response.status
            errorMessage = response.errorMessage
            addToCartBundleDataModel = mapAddToCartBundleData(response.data)
        }
    }

    private fun mapAddToCartBundleData(dataResponse: AddToCartBundleDataResponse): AddToCartBundleDataModel {
        return AddToCartBundleDataModel().apply {
            success = dataResponse.success
            message = dataResponse.messages
            val productDataList = mutableListOf<ProductDataModel>()
            dataResponse.data.forEach { productDataResponse ->
                val productData = mapProductData(productDataResponse)
                productDataList.add(productData)
            }
            data = productDataList
        }
    }

    private fun mapProductData(productDataResponse: ProductDataResponse): ProductDataModel {
        return ProductDataModel().apply {
            cartId = productDataResponse.cartId
            customerId = productDataResponse.customerId
            notes = productDataResponse.notes
            productId = productDataResponse.productId
            quantity = productDataResponse.quantity
            shopId = productDataResponse.shopId
            warehouseId = productDataResponse.warehouseId
        }
    }
}
