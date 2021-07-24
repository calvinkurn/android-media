package com.tokopedia.graphql.coroutines.domain.interactor

import com.tokopedia.graphql.util.LoggingUtils.logGqlErrorNetwork
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class CoroutineUseCase<P, out R: Any> constructor(
    private val dispatcher: CoroutineDispatcher
) : UseCase() {

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

    /*
    * Getting the request result without state Result<>
    * */
    suspend fun statelessResult(params: P? = null): R {
        return withContext(dispatcher) {
            execute(params)
        }
    }

}