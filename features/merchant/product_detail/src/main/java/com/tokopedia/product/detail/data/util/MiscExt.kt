package com.tokopedia.product.detail.data.util

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo

inline fun <reified T> GraphqlResponse.getSuccessData(): T {
    val error = getError(T::class.java)
    if (error == null || error.isEmpty()){
        return getData(T::class.java)
    } else {
        throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
    }
}

val WarehouseInfo.origin: String?
    get() {
        return if (districtId.isNotBlank() && (postalCode.isNotBlank() || geoLocation.isNotBlank())){
            arrayOf(districtId, postalCode, geoLocation).joinToString("|")
        } else null
    }