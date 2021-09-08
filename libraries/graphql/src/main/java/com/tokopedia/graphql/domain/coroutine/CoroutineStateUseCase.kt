package com.tokopedia.graphql.domain.coroutine

import com.tokopedia.graphql.domain.GqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class CoroutineStateUseCase<P, out R: Any> constructor(
    private val dispatcher: CoroutineDispatcher
) : GqlUseCase<P, R>() {

    /*
    * Executes the use case with state synchronously
    *
    * @param params the input parameters to run the use case with
    * @return an generic class with state comes from R
    * */
    suspend fun invoke(params: P): Result<R> {
        return try {
            withContext(dispatcher) {
                Success(execute(params))
            }
        } catch (e: Exception) {
            Fail(e)
        }
    }

}