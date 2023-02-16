package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addpin.data.AddPinPojo
import com.tokopedia.profilecompletion.addpin.data.CheckPinPojo
import javax.inject.Inject

class CheckPinUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, String>, CheckPinPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          query checkPin(${'$'}pin: String!){
            check_pin (pin: ${'$'}pin){
              valid
              error_message
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Map<String, String>): CheckPinPojo {
        return repository.request(graphqlQuery(), params)
    }

}
