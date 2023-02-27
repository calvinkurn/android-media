package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addbod.data.UserProfileCompletionUpdateBodData
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import javax.inject.Inject

class AddBodUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, UserProfileCompletionUpdateBodData>(dispatchers.io) {
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

    override suspend fun execute(params: String): UserProfileCompletionUpdateBodData {
        val parameter = mapOf(ProfileCompletionQueryConstant.PARAM_BOD to params)
        return repository.request(graphqlQuery(), parameter)
    }

}
