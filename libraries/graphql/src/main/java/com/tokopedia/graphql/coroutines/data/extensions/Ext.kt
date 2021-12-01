package com.tokopedia.graphql.coroutines.data.extensions

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.util.LoggingUtils
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Suppress("UNCHECKED_CAST")
suspend inline fun <P, reified R> GraphqlRepository.request(
    query: String,
    params: P
): R {
    val variables = when (params) {
        is Map<*, *> -> params as Map<String, Any>
        is Unit -> emptyMap()
        is GqlParam -> params.toMapParam()
        else -> throw IllegalArgumentException(
            "Graphql only supports Map<String, Any>, Unit, and GqlParam as the parameters"
        )

    }
    val request = GraphqlRequest(query, R::class.java, variables)
    val response = response(listOf(request))

    return response.getSuccessData()
}

inline fun <P, reified R> GraphqlRepository.requestAsFlow(
    query: String,
    params: P
): Flow<R> {
    return flow {
        emit(request(query, params))
    }
}


inline fun <reified T> GraphqlResponse.getSuccessData(): T {
    val error = getError(T::class.java)
    if (error == null || error.isEmpty()) {
        return getData(T::class.java)
    } else {
        val errorMessage = error.mapNotNull { it.message }.joinToString(separator = ", ")
        LoggingUtils.logGqlErrorBackend(
            "getSuccessData",
            "",
            errorMessage,
            httpStatusCode.toString()
        )
        throw MessageErrorException(errorMessage)
    }
}