package com.tokopedia.inbox.universalinbox.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxCounterWrapperResponse
import javax.inject.Inject

class UniversalInboxGetAllCounterUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, UniversalInboxAllCounterResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query GetAllCounter($$PARAM_INPUT: NotificationRequest) {
          notifications(input: $$PARAM_INPUT) {
            inbox {
              talk
              ticket
              review
            }
            chat {
              unreadsSeller
              unreadsUser
            }
            notifcenter_unread {
              notif_unread
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): UniversalInboxAllCounterResponse {
        val response = repository.request<Map<String, Any>, UniversalInboxCounterWrapperResponse>(
            query = graphqlQuery(),
            params = mapOf(SHOP_ID to params)
        )
        return response.allCounter
    }

    companion object {
        private const val PARAM_INPUT = "input"
        private const val SHOP_ID = "shopId"
    }
}
