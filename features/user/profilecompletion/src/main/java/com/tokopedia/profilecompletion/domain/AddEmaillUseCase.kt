package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addemail.data.AddEmailPojo
import javax.inject.Inject

class AddEmailUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, String>, AddEmailPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation add_email(${'$'}email: String!, ${'$'}otpCode: String!, ${'$'}validateToken: String) {
            userProfileCompletionUpdate(email: ${'$'}email, otpCode: ${'$'}otpCode, validateToken: ${'$'}validateToken) {
              isSuccess
              emailMessage
              completionScore
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Map<String, String>): AddEmailPojo {
        return repository.request(graphqlQuery(), params)
    }

}
