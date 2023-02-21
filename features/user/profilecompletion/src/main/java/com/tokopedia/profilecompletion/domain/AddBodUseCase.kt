package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addbod.data.UserProfileCompletionUpdateBodData
import javax.inject.Inject

class AddBodUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, String>, UserProfileCompletionUpdateBodData>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation add_bod(${'$'}birthdate: String!) {
            userProfileCompletionUpdate(birthdate: ${'$'}birthdate) {
              isSuccess
              birthDateMessage
              completionScore
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Map<String, String>): UserProfileCompletionUpdateBodData {
        return repository.request(graphqlQuery(), params)
    }

}
