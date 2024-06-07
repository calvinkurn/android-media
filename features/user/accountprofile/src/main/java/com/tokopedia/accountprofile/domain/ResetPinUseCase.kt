package com.tokopedia.accountprofile.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.accountprofile.settingprofile.changepin.data.ResetPinResponse
import com.tokopedia.accountprofile.data.ProfileCompletionQueryConstant
import javax.inject.Inject

class ResetPinUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, ResetPinResponse>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation resetPin(${'$'}validate_token: String!){
              reset_pin(input:{validate_token: ${'$'}validate_token}) {
                  success
                  errors {
                      message
                  }
              }
          }
        """.trimIndent()

    override suspend fun execute(params: String): ResetPinResponse {
        val parameter = mapOf(ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN to params)
        return repository.request(graphqlQuery(), parameter)
    }
}
