package com.tokopedia.loginregister.login_sdk.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.login_sdk.data.ValidateClientParam
import com.tokopedia.loginregister.login_sdk.data.ValidateClientResponse
import javax.inject.Inject

class ValidateClientUseCase @Inject constructor(
    @ApplicationContext
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ValidateClientParam, ValidateClientResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query.getQuery()

    override suspend fun execute(params: ValidateClientParam): ValidateClientResponse {
        return repository.request(query, params)
    }

    private val query = object : GqlQueryInterface {
        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = """
            query $OPERATION_NAME(${'$'}client_id: String!, ${'$'}app_signature: String!, ${'$'}package_name: String!, ${'$'}redirect_uri: String!){
                validate_client_signature(
                    client_id:${'$'}client_id,
                    app_signature:${'$'}app_signature,
                    package_name:${'$'}package_name,
                    redirect_uri:${'$'}redirect_uri
                ) {
                    is_valid
                    app_name
                    error
                }
            }""".trimIndent()

        override fun getTopOperationName(): String = OPERATION_NAME
    }

    companion object {
        const val OPERATION_NAME = "validate_client_signature"
    }
}
