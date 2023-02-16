package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addpin.data.AddPinPojo
import javax.inject.Inject

class CreatePinUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, String>, AddPinPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation createPin(${'$'}token: String!){
            create_pin(input:{validate_token: ${'$'}token}) {
              success
              errors {
                message
              }
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Map<String, String>): AddPinPojo {
        return repository.request(graphqlQuery(), params)
    }

}
