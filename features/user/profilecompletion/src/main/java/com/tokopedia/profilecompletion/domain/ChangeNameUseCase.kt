package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNamePojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import javax.inject.Inject

class ChangeNameUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, ChangeNamePojo>(dispatchers.io) {
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

    override suspend fun execute(params: String): ChangeNamePojo {
        val parameter = mapOf(ProfileCompletionQueryConstant.PARAM_NAME to params)
        return repository.request(graphqlQuery(), parameter)
    }
}
