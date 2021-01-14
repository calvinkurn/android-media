package com.tokopedia.product.detail.data.util

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException

inline fun <reified T> GraphqlResponse.getSuccessData(): T {
    val error = getError(T::class.java)
    if (error == null || error.isEmpty()) {
        return getData(T::class.java)
    } else {
        throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
    }
}