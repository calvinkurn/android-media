package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addpin.data.AddPinPojo
import com.tokopedia.profilecompletion.addpin.data.StatusPinPojo
import javax.inject.Inject

class StatusPinUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, StatusPinPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          query {
            status_pin {
              is_registered
              is_blocked
              is_phone_number_exist
              is_phone_number_verified
              msisdn
              error_message
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Unit): StatusPinPojo {
        return repository.request(graphqlQuery(), params)
    }

}
