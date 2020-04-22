package com.tokopedia.settingnotif.usersetting.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams

object BaseRequestUseCase {

    suspend inline fun <reified T> request(
            query: String,
            repository: GraphqlRepository,
            requestParams: RequestParams = RequestParams.EMPTY
    ): T {
        val request = GraphqlRequest(query, T::class.java, requestParams.parameters)
        val response = repository.getReseponse(listOf(request))
        val error = response.getError(T::class.java)

        if (error == null || error.isEmpty()) {
            return response.getData(T::class.java) as T
        } else {
            throw MessageErrorException(
                    error.mapNotNull { it.message }.joinToString(separator = ", ")
            )
        }
    }

}