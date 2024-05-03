package com.tokopedia.loginregister.login.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import javax.inject.Inject

class LoginTiktokUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<LoginTiktokUseCase.Param, LoginTokenPojoV2>(dispatcher.io) {

    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: LoginTiktokUseCase.Param): LoginTokenPojoV2 {
        val result: LoginTokenPojoV2 = repository.request(gqlQuery, params)
        return result
    }

    val gqlQuery = object : GqlQueryInterface {
        override fun getQuery(): String = """
        mutation $OPERATION_NAME(${'$'}grant_type: String!, ${'$'}code: String!, ${'$'}social_type: String!, ${'$'}code_verifier: String!, ${'$'}redirect_uri: String!){
            login_token_v2(
                input: {
                    grant_type: ${'$'}grant_type
                    code: ${'$'}code
                    social_type: ${'$'}social_type
                    code_verifier: ${'$'}code_verifier
                    redirect_uri: ${'$'}redirect_uri
                }
            ) {
                acc_sid
                access_token
                expires_in
                refresh_token
                sid
                token_type
                sq_check
                action
                errors {
                    name
                    message
                }
                event_code
            }
        }
        """.trimIndent()

        override fun getOperationNameList(): List<String> {
            return listOf(OPERATION_NAME)
        }

        override fun getTopOperationName(): String {
            return OPERATION_NAME
        }
    }

    data class Param(
        @SerializedName("grant_type")
        var grantType: String = "extension",
        @SerializedName("social_type")
        val socialType: String = "17",
        @SerializedName("code_verifier")
        val codeVerifier: String,
        @SerializedName("redirect_uri")
        val redirectUri: String,
        @SerializedName("code")
        val code: String
    ): GqlParam

    companion object {
        private const val OPERATION_NAME = "LoginTiktok"
    }
}
