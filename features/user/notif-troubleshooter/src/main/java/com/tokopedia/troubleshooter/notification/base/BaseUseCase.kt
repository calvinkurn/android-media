package com.tokopedia.troubleshooter.notification.base

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams

abstract class BaseUseCase<in T, out P> {

    /**
     * Override this to set the code to be executed.
     */
    abstract suspend fun execute(params: T): P

    /** Executes the use case synchronously
     *
     * @param params the input parameters to run the use case with
     * @return an generic class comes from P
     *
     */
    suspend operator fun invoke(params: T): P {
        return execute(params)
    }

    // execute wrapper
    suspend inline fun <reified T> execute(
            query: String,
            repository: GraphqlRepository,
            requestParams: RequestParams
    ): T {
        val request = GraphqlRequest(query, T::class.java, requestParams.parameters)
        val response = repository.getReseponse(listOf(request))
        val error = response.getError(T::class.java)

        if (error == null || error.isEmpty()) {
            return response.getData(T::class.java) as T
        } else {
            throw MessageErrorException(
                    error.mapNotNull {
                        it.message
                    }.joinToString(separator = ", ")
            )
        }
    }

}