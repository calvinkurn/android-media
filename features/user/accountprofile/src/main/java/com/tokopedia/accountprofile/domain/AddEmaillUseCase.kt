package com.tokopedia.accountprofile.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.accountprofile.settingprofile.addemail.data.AddEmailPojo
import com.tokopedia.accountprofile.data.AddEmailParam
import javax.inject.Inject

class AddEmailUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<AddEmailParam, AddEmailPojo>(dispatchers.io) {
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

    override suspend fun execute(params: AddEmailParam): AddEmailPojo {
        return repository.request(graphqlQuery(), params)
    }

}
