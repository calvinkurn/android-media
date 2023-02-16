package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.changepin.data.ResetPinResponse
import javax.inject.Inject

class ResetPinUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, String>, ResetPinResponse>(dispatchers.io) {
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

    override suspend fun execute(params: Map<String, String>): ResetPinResponse {
        return repository.request(graphqlQuery(), params)
    }
}
