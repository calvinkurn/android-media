package com.tokopedia.graphql.domain.flow

import com.tokopedia.graphql.domain.GqlUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<P, out R: Any> constructor(
    private val dispatcher: CoroutineDispatcher
) : GqlUseCase<P, Flow<R>>() {

    /*
    * Executes the use case based on dispatcher's flow
    *
    * @param params the input parameters to run the use case with
    * @return a flowable generic class comes from R
    * */
    suspend operator fun invoke(params: P): Flow<R> {
        return execute(params)
            .flowOn(dispatcher)
    }

}