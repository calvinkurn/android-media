package com.tokopedia.graphql.domain.coroutine

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.domain.GqlUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class CoroutineUseCase<Input, Output : Any> constructor(
    private val dispatcher: CoroutineDispatcher
) : GqlUseCase<Input, Output>() {


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