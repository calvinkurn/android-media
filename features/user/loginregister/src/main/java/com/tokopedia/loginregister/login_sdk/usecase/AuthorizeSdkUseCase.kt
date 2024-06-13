package com.tokopedia.loginregister.login_sdk.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.login_sdk.data.SdkAuthorizeInput
import com.tokopedia.loginregister.login_sdk.data.SdkAuthorizeParam
import com.tokopedia.loginregister.login_sdk.data.SdkAuthorizeResponse
import javax.inject.Inject

class AuthorizeSdkUseCase @Inject constructor(
    @ApplicationContext
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<SdkAuthorizeParam, SdkAuthorizeResponse>(dispatchers.io) {
    override fun graphqlQuery(): String = query.getQuery()

    override suspend fun execute(params: SdkAuthorizeParam): SdkAuthorizeResponse {
        val newParam = SdkAuthorizeInput(input = params)
        return repository.request(query, newParam)
    }

    private val query = object : GqlQueryInterface {

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = """
            query $OPERATION_NAME(${'$'}input: OauthAuthorizeParam!){
                oauth_authorize(input:${'$'}input) {
                    expires_in
                    code
                    redirect_uri
                    error
                    is_success
                }
            }""".trimIndent()

        override fun getTopOperationName(): String = OPERATION_NAME
    }

    companion object {
        const val OPERATION_NAME = "oauth_authorize"
    }
}
