package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.data.AddEmailParam
import com.tokopedia.privacycenter.data.AddEmailResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class DsarAddEmailUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<AddEmailParam, AddEmailResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        mutation add_email(${'$'}email: String!, ${'$'}otpCode: String!, ${'$'}validateToken: String) {
          userProfileCompletionUpdate(email: ${'$'}email, otpCode: ${'$'}otpCode, validateToken: ${'$'}validateToken) {
            isSuccess
            emailMessage
            completionScore
          }
        }
    """.trimIndent()

    override suspend fun execute(params: AddEmailParam): AddEmailResponse {
        val result: AddEmailResponse = repository.request(graphqlQuery(), params)
        if (result.data.isSuccess) {
            saveEmailToUserSession(params.email)
        }
        return result
    }

    private fun saveEmailToUserSession(email: String) {
        userSession.email = email
    }
}
