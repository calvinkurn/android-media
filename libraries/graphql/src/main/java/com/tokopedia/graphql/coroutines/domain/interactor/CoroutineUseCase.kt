package com.tokopedia.graphql.coroutines.domain.interactor

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.tokopedia.graphql.util.LoggingUtils.logGqlErrorNetwork as logGqlErrorNetwork

abstract class CoroutineUseCase<P, out R: Any> constructor(
    private val dispatcher: CoroutineDispatcher
) {

    /*
    * override this to set the graphql query
    * */
    protected abstract fun graphqlQuery(): String

    /*
    * override this to set the code to be executed
    * */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: P?): R

    /*
    * Executes the use case synchronously
    *
    * @param params the input parameters to run the use case with
    * @return an generic class comes from P
    * */
    suspend operator fun invoke(params: P? = null): Result<R> {
        return try {
            withContext(dispatcher) {
                Success(execute(params))
            }
        } catch (e: Exception) {
            logGqlErrorNetwork("CoroutineUseCase", "", e)
            Fail(e)
        }
    }

    protected suspend inline fun <reified T> GraphqlRepository.request(
        params: Map<String, Any>?
    ): T {
        val request = GraphqlRequest(graphqlQuery(), T::class.java, params?: mapOf())
        val response = getReseponse(listOf(request))
        return response.getSuccessData()
    }

}