package com.tokopedia.graphql.domain.coroutine

import com.tokopedia.graphql.domain.GqlUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class CoroutineUseCase<Input, Output : Any> constructor(
    private val dispatcher: CoroutineDispatcher
) : GqlUseCase<Input, Output>() {

    /*
    * Executes the use case synchronously
    *
    * @param params the input parameters to run the use case with
    * @return an generic class comes from R
    * */
    suspend operator fun invoke(params: Input): Output {
        return withContext(dispatcher) {
            execute(params)
        }
    }

}