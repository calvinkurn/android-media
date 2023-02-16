package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addpin.data.ChangePinPojo
import javax.inject.Inject

class ChangePinUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, String>, ChangePinPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation updatePin(${'$'}pin: String!, ${'$'}pinConfirm: String!, ${'$'}pinOld: String!){
              update_pin(input:{pin: ${'$'}pin, pin_confirm: ${'$'}pinConfirm, pin_old: ${'$'}pinOld}) {
                  success
                  errors {
                      message
                  }
              }
          }
        """.trimIndent()

    override suspend fun execute(params: Map<String, String>): ChangePinPojo {
        return repository.request(graphqlQuery(), params)
    }

}
