package com.tokopedia.graphql.domain.coroutine

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GqlUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class CoroutineUseCase<P, out R: Any> constructor(
    repository: GraphqlRepository,
    private val dispatcher: CoroutineDispatcher
) : GqlUseCase<P, R>(repository) {

    /*
    * Executes the use case synchronously
    *
    * @param params the input parameters to run the use case with
    * @return an generic class comes from R
    * */
    suspend operator fun invoke(params: P): R {
        return withContext(dispatcher) {
            execute(params)
        }
    }

}