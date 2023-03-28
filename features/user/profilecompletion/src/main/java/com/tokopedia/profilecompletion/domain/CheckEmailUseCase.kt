package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addemail.data.CheckEmailPojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import javax.inject.Inject

class CheckEmailUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, CheckEmailPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation check_email(${'$'}email: String!) {
            userProfileCompletionValidate(email: ${'$'}email) {
              isValid
              emailMessage
            }
          }
        """.trimIndent()

    override suspend fun execute(params: String): CheckEmailPojo {
        val parameter = mapOf(ProfileCompletionQueryConstant.PARAM_EMAIL to params)
        return repository.request(graphqlQuery(), parameter)
    }

}
