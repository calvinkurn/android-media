package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderPojo
import javax.inject.Inject

class ChangeGenderUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Int>, ChangeGenderPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation change_gender(${'$'}gender: Int!) {
            userProfileCompletionUpdate(gender: ${'$'}gender) {
              isSuccess
              genderMessage
              completionScore
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Map<String, Int>): ChangeGenderPojo {
        return repository.request(graphqlQuery(), params)
    }
}
