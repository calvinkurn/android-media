package com.tokopedia.graphql.domain.flow

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GqlFlowUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowStateUseCase<Input, out Output : Any> constructor(
    repository: GraphqlRepository,
    private val dispatcher: CoroutineDispatcher
) : GqlFlowUseCase<Input, Output>(repository) {


    /*
    * override this to set the code to be executed
    * */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: Input): Flow<Result<Output>>

    /*
    * Executes the use case based on dispatcher's flow with state
    *
    * @param params the input parameters to run the use case with
    * @return an generic class with state and flowable comes from R
    * */
    suspend operator fun invoke(params: Input): Flow<Result<Output>> {
        return execute(params)
            .catch { e -> emit(Fail(e)) }
            .flowOn(dispatcher)
    }

}