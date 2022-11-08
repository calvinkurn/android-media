package com.tokopedia.profilecompletion.addphone.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addphone.data.UserValidatePojo
import javax.inject.Inject

class UserProfileValidateUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, UserValidatePojo>(dispatchers.io) {

    override fun graphqlQuery(): String =
        """
            mutation userProfileValidate(${'$'}phone: String) {
                userProfileValidate(phone: ${'$'}phone) {
                    isValid,
                    message
                }
            }
        """.trimIndent()

    override suspend fun execute(params: String): UserValidatePojo {
        val parameter = mapOf(KEY_PHONE to params)
        return repository.request(graphqlQuery(), parameter)
    }

    companion object {
        private const val KEY_PHONE = "phone"
    }

}
