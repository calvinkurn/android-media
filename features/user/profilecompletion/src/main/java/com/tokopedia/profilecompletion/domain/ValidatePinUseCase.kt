package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addpin.data.ValidatePinPojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import javax.inject.Inject

class ValidatePinUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, ValidatePinPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          query validatePin(${'$'}pin: String!) {
            validate_pin(pin: ${'$'}pin) {
              valid
              error_message
              pin_attempted
              max_pin_attempt
            }
          }
        """.trimIndent()

    override suspend fun execute(params: String): ValidatePinPojo {
        val parameter = mapOf(ProfileCompletionQueryConstant.PARAM_PIN to params)
        return repository.request(graphqlQuery(), parameter)
    }
}
