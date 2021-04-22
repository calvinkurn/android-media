package com.tokopedia.atc_common.domain.mapper

import com.tokopedia.atc_common.data.model.response.atcexternal.AddToCartExternalDataResponse
import com.tokopedia.atc_common.data.model.response.atcexternal.AddToCartExternalGqlResponse
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalDataModel
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalModel
import javax.inject.Inject

class AddToCartExternalDataMapper @Inject constructor() {

    fun map(response: AddToCartExternalGqlResponse): AddToCartExternalModel {
        return AddToCartExternalModel().apply {
            message = response.response.data.message
            success = response.response.data.success
            data = mapData(response.response.data.data)
        }
    }

    private fun mapData(dataResponse: AddToCartExternalDataResponse): AddToCartExternalDataModel {
        return AddToCartExternalDataModel().apply {
            productId = dataResponse.productId
            productName = dataResponse.productName
            quantity = dataResponse.quantity
            price = dataResponse.price
            category = dataResponse.category
            shopId = dataResponse.shopId
            shopType = dataResponse.shopType
            shopName = dataResponse.shopName
            picture = dataResponse.picture
            url = dataResponse.url
            cartId = dataResponse.cartId
            brand = dataResponse.brand
            categoryId = dataResponse.categoryId
            variant = dataResponse.variant
            trackerAttribution = dataResponse.trackerAttribution
            isMultiOrigin = dataResponse.isMultiOrigin
            isFreeOngkir = dataResponse.isFreeOngkir
            isFreeOngkirExtra = dataResponse.isFreeOngkirExtra
        }
    }
}