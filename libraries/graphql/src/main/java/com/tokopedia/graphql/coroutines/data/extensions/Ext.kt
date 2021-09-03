package com.tokopedia.graphql.coroutines.data.extensions

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.util.LoggingUtils
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

suspend inline fun <reified R, P> GraphqlRepository.request(
    query: String,
    params: P
): R {
    val variables = when (params) {
        is Map<*, *> -> params as Map<String, Any>
        is Unit -> emptyMap()
        else -> throw IllegalArgumentException("Graphql only supports Map<String, Any> and Unit as a param")
    }
    val request = GraphqlRequest(query, R::class.java, variables)
    val response = getReseponse(listOf(request))

    return response.getSuccessData()
}

inline fun <reified R, P> GraphqlRepository.requestAsFlow(
    query: String,
    params: P
): Flow<R> {
    return flow {
        request(query, params)
    }
}


inline fun <reified T> GraphqlResponse.getSuccessData(): T {
    val error = getError(T::class.java)
    if (error == null || error.isEmpty()){
        return getData(T::class.java)
    } else {
        val errorMessage = error.mapNotNull { it.message }.joinToString(separator = ", ")
        LoggingUtils.logGqlErrorBackend("getSuccessData", "", errorMessage, httpStatusCode.toString())
        throw MessageErrorException(errorMessage)
    }
}