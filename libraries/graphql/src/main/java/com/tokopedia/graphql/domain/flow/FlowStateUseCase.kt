package com.tokopedia.graphql.domain.flow

import com.tokopedia.graphql.domain.GqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowStateUseCase<P, out R: Any> constructor(
    private val dispatcher: CoroutineDispatcher
) : GqlUseCase<P, Flow<Result<R>>>() {

    /*
    * Executes the use case based on dispatcher's flow with state
    *
    * @param params the input parameters to run the use case with
    * @return an generic class with state and flowable comes from R
    * */
    suspend operator fun invoke(params: P): Flow<Result<R>> {
        return execute(params)
            .catch { e -> emit(Fail(e)) }
            .flowOn(dispatcher)
    }

}