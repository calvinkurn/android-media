package com.tokopedia.atc_common.domain.mapper

import com.tokopedia.atc_common.data.model.response.atcexternal.AddToCartExternalGqlResponse
import com.tokopedia.atc_common.data.model.response.atcexternal.DataAddToCartResponse
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalDataModel
import com.tokopedia.atc_common.domain.model.response.atcexternal.DataAddToCartModel
import javax.inject.Inject

class AddToCartExternalDataMapper @Inject constructor() {

    fun map(response: AddToCartExternalGqlResponse): AddToCartExternalDataModel {
        return AddToCartExternalDataModel().apply {
            message = response.response.data.message
            success = response.response.data.success
            data = mapData(response.response.data.data)
        }
    }

    private fun mapData(dataAddToCartResponse: DataAddToCartResponse): DataAddToCartModel {
        return DataAddToCartModel().apply {
            productId = dataAddToCartResponse.productId
            productName = dataAddToCartResponse.productName
            quantity = dataAddToCartResponse.quantity
            price = dataAddToCartResponse.price
            category = dataAddToCartResponse.category
            shopId = dataAddToCartResponse.shopId
            shopType = dataAddToCartResponse.shopType
            shopName = dataAddToCartResponse.shopName
            picture = dataAddToCartResponse.picture
            url = dataAddToCartResponse.url
            cartId = dataAddToCartResponse.cartId
            brand = dataAddToCartResponse.brand
            categoryId = dataAddToCartResponse.categoryId
            variant = dataAddToCartResponse.variant
            trackerAttribution = dataAddToCartResponse.trackerAttribution
            isMultiOrigin = dataAddToCartResponse.isMultiOrigin
            isFreeOngkir = dataAddToCartResponse.isFreeOngkir
        }
    }
}