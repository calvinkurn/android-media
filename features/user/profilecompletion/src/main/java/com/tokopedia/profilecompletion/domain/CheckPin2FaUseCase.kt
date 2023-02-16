package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addpin.data.CheckPinPojo
import javax.inject.Inject

class CheckPin2FaUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, CheckPinPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          query resetUserPin(
              ${'$'}user_id: Int!,
              ${'$'}validate_token: String!,
              ${'$'}grant_type: String!
          ){
              resetUserPin(
                  user_id: ${'$'}user_id,
                  validate_token: ${'$'}validate_token,
                  grant_type: ${'$'}grant_type
              ) {
                  is_success
                  user_id 
                  access_token
                  sid
                  refresh_token
                  expires_in
                  error
              }
          }
        """.trimIndent()

    override suspend fun execute(params: Map<String, Any>): CheckPinPojo {
        return repository.request(graphqlQuery(), params)
    }

}
