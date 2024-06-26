package com.tokopedia.accountprofile.settingprofile.addphone.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.accountprofile.settingprofile.addphone.data.AddPhonePojo
import com.tokopedia.accountprofile.settingprofile.addphone.domain.param.UserProfileUpdateParam
import javax.inject.Inject

class UserProfileUpdateUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<UserProfileUpdateParam, AddPhonePojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation userProfileUpdate(${'$'}phone: String!, ${'$'}currValidateToken: String!) {
            userProfileUpdate(phone: ${'$'}phone, currValidateToken: ${'$'}currValidateToken) {
              isSuccess
              errors
            }
          }
        """.trimIndent()

    override suspend fun execute(params: UserProfileUpdateParam): AddPhonePojo {
        return repository.request(graphqlQuery(), params)
    }

}
