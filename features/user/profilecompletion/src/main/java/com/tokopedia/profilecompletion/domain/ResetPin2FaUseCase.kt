package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.changepin.data.ResetPin2FaPojo
import com.tokopedia.profilecompletion.data.ResetPin2FAParam
import javax.inject.Inject

class ResetPin2FaUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ResetPin2FAParam, ResetPin2FaPojo>(dispatchers.io) {
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

    override suspend fun execute(params: ResetPin2FAParam): ResetPin2FaPojo {
        return repository.request(graphqlQuery(), params)
    }
}
