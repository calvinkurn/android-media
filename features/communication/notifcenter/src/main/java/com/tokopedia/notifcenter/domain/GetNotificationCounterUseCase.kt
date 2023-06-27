package com.tokopedia.notifcenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.notifcenter.data.entity.NotificationResponse
import javax.inject.Inject

class GetNotificationCounterUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<String, NotificationResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query notifications_inbox_counter(${'$'}$PARAM_INPUT: String) {
          notifications(input: {shop_id: ${'$'}$PARAM_INPUT}){
            total_cart
            inbox_counter{
              buyer{
                notifcenter_int
              }
              seller{
                notifcenter_int
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): NotificationResponse {
        return repository.request(graphqlQuery(), getParams(params))
    }

    private fun getParams(shopId: String): Map<String, Any> {
        return mapOf(
            PARAM_INPUT to shopId
        )
    }

    companion object {
        private const val PARAM_INPUT = "input"
    }
}
