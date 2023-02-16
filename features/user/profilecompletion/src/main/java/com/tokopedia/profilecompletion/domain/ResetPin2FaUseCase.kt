package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.changepin.data.ResetPin2FaPojo
import com.tokopedia.profilecompletion.changepin.data.ResetPinResponse
import javax.inject.Inject

class ResetPin2FaUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, ResetPin2FaPojo>(dispatchers.io) {
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

    override suspend fun execute(params: Map<String, Any>): ResetPin2FaPojo {
        return repository.request(graphqlQuery(), params)
    }
}
