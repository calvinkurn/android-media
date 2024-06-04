package com.tokopedia.accountprofile.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.accountprofile.settingprofile.addpin.data.CheckPinPojo
import com.tokopedia.accountprofile.data.ProfileCompletionQueryConstant
import javax.inject.Inject

class CheckPinUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, CheckPinPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          query checkPin(${'$'}pin: String!){
            check_pin (pin: ${'$'}pin){
              valid
              error_message
            }
          }
        """.trimIndent()

    override suspend fun execute(params: String): CheckPinPojo {
        val parameter = mapOf(ProfileCompletionQueryConstant.PARAM_PIN to params)
        return repository.request(graphqlQuery(), parameter)
    }

}
