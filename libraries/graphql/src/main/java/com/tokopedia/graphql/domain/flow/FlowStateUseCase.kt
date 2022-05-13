package com.tokopedia.graphql.domain.flow

import com.tokopedia.gql_query_annotation.GqlQueryInterface
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
     * It is a migration technique to avoid breaking change for existing implementations.
     * Migration plan:
     * - Change graphqlQuery function implementation to this variable
     * - Delete graphqlQuery
     * - Make graphqlQueryInterface abstract
     * */
    override fun graphqlQueryInterface(): GqlQueryInterface = object : GqlQueryInterface {
        override fun getOperationNameList(): List<String> = emptyList()
        override fun getQuery(): String = graphqlQuery()
        override fun getTopOperationName(): String = ""
    }

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