package com.tokopedia.graphql.coroutines.domain.interactor

import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<P, out R: Any> constructor(
    private val dispatcher: CoroutineDispatcher
) : UseCase() {

    /*
    * override this to set the code to be executed
    * */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: P): Flow<Result<R>>

    /*
    * Executes the use case based on dispatcher's flow
    *
    * @param params the input parameters to run the use case with
    * @return an generic class comes from P
    * */
    suspend operator fun invoke(params: P): Flow<Result<R>> {
        return execute(params)
            .catch { e -> emit(Fail(e)) }
            .flowOn(dispatcher)
    }

}