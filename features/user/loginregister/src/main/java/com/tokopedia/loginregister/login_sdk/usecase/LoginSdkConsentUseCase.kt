package com.tokopedia.loginregister.login_sdk.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.login_sdk.data.SdkConsentParam
import com.tokopedia.loginregister.login_sdk.data.SdkConsentResponse
import javax.inject.Inject

class LoginSdkConsentUseCase @Inject constructor(
    @ApplicationContext
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<SdkConsentParam, SdkConsentResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query.getQuery()

    override suspend fun execute(params: SdkConsentParam): SdkConsentResponse {
        return repository.request(query, params)
    }

    private val query = object : GqlQueryInterface {
        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = """
            query $OPERATION_NAME(${'$'}client_id: String!){
                oauth_check_consent(client_id:${'$'}client_id) {
                    show_consent
                    user_info{
                        fullname
                        first_name
                        email
                        profile_picture
                        phone_number
                    }
                    client_info {
                        image_url
                        app_name
                        redirect_uri
                        is_external
                    }
                    term_and_privacy_url {
                        tokopedia_tnc_id
                        tnc_url
                        privacy_url
                        purpose
                    }
                    consent
                    is_success
                    error
                }
            }""".trimIndent()

        override fun getTopOperationName(): String = OPERATION_NAME
    }

    companion object {
        const val OPERATION_NAME = "oauth_check_consent"
    }
}
