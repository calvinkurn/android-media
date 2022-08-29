package com.tokopedia.graphql.domain.flow

import com.tokopedia.graphql.domain.GqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn


/**
 * When overriding execute, it is required to map the flow to Result.Success
 * Example can be found in [NoParamExample.kt] in test source files
 * */
abstract class FlowStateUseCase<Input, Output : Any> constructor(
    private val dispatcher: CoroutineDispatcher
) : GqlUseCase<Input, Flow<Result<Output>>>() {

    /**
     * Executes the use case based on dispatcher's flow with state
     *
     * @param params the input parameters to run the use case with
     * @return a generic class with state and flowable comes from R
     * */
    suspend operator fun invoke(params: Input): Flow<Result<Output>> {
        return execute(params)
            .catch { e -> emit(Fail(e)) }
            .flowOn(dispatcher)
    }

}