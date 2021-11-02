package com.tokopedia.graphql.domain.flow

import com.tokopedia.graphql.domain.GqlUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<Input, Output> constructor(
    private val dispatcher: CoroutineDispatcher
) : GqlUseCase<Input, Flow<Output>>() {

    /*
    * Executes the use case based on dispatcher's flow
    *
    * @param params the input parameters to run the use case with
    * @return a flowable generic class comes from R
    * */
    suspend operator fun invoke(params: Input): Flow<Output> {
        return execute(params)
            .flowOn(dispatcher)
    }

}