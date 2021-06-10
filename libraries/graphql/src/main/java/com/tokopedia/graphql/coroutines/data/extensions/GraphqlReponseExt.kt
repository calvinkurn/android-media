package com.tokopedia.graphql.coroutines.data.extensions

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.util.LoggingUtils
import com.tokopedia.network.exception.MessageErrorException

inline fun <reified T> GraphqlResponse.getSuccessData(): T {
    val error = getError(T::class.java)
    if (error == null || error.isEmpty()){
        return getData(T::class.java)
    } else {
        val errorMessage = error.mapNotNull { it.message }.joinToString(separator = ", ")
        LoggingUtils.logGqlErrorBackend("getSuccessData", "", errorMessage)
        throw MessageErrorException(errorMessage)
    }
}