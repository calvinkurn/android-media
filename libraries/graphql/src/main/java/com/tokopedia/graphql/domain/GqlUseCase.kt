package com.tokopedia.graphql.domain

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * This is the base class for the domain layer by using the GraphQL service.
 * This class is the result of an improvement from the existing GraphqlUseCase (or related).
 *
 * this class is created in order to achieve flexibility in further development of domain layer.
 * currently, [GqlUseCase] is used in base class for [CoroutineUseCase], [CoroutineStateUseCase],
 * [FlowUseCase], and [FlowStateUseCase]
 *
 * This class has 2 functions in it:
 * - graphqlQuery()
 *   This function defines the query graphql that will be used to determine what data to get.
 *
 * - execute()
 *   This function is to determine the target request that will be managed to get data
 *   from certain sources, both network and local sources.
 *
 */
abstract class GqlUseCase<Input, Output> {

    /*
    * override this to set the string graphql query
    * */
    protected open fun graphqlQuery(): String = ""

    /*
    * override this to set the interface graphql query object
    * */
    protected open fun gqlQuery(): GqlQueryInterface? = null

    /*
    * override this to set the code to be executed
    * */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: Input): Output

}
