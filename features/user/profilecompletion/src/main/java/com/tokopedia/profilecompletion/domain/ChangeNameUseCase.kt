package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderPojo
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNamePojo
import javax.inject.Inject

class ChangeNameUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, String>, ChangeNamePojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation change_name(${'$'}name: String!) {
            userProfileUpdate(fullname: ${'$'}name) {
              isSuccess
              completionScore
              errors
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Map<String, String>): ChangeNamePojo {
        return repository.request(graphqlQuery(), params)
    }
}
