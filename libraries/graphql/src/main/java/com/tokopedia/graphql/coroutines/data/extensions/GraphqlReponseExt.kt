package com.tokopedia.graphql.coroutines.data.extensions

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException

inline fun <reified T> GraphqlResponse.getSuccessData(kClass: Class<T> = T::class.java): T {
    val error = getError(kClass)
    if (error == null || error.isEmpty()){
        return getData(kClass)
    } else {
        throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
    }
}