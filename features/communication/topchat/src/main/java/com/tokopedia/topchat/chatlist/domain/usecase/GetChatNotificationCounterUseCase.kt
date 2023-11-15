package com.tokopedia.topchat.chatlist.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.common.data.TopChatResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GetChatNotificationCounterUseCase @Inject constructor(
    @ApplicationContext private val graphql: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers
) {

    private val _notificationsCounterFlow = MutableStateFlow<TopChatResult<NotificationsPojo>>(
        TopChatResult.Loading
    )

    fun observe(): Flow<TopChatResult<NotificationsPojo>> = _notificationsCounterFlow.asStateFlow()

    private fun graphqlQuery(): String = """
        query get_chat_notif($$PARAM_SHOP_ID: String) {
          notifications(input: {shop_id: $$PARAM_SHOP_ID}) {
            chat {
              unreadsSeller
              unreadsUser
            }
          }
        }
    """.trimIndent()

    suspend fun refreshCounter(params: String) {
        withContext(dispatchers.io) {
            try {
                _notificationsCounterFlow.value = TopChatResult.Loading
                val param = mapOf(PARAM_SHOP_ID to params)
                val response = graphql.request<Map<String, Any>, NotificationsPojo>(
                    graphqlQuery(),
                    param
                )
                _notificationsCounterFlow.value = TopChatResult.Success(response)
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                _notificationsCounterFlow.value = TopChatResult.Error(throwable)
            }
        }
    }

    companion object {
        private const val PARAM_SHOP_ID = "shop_id"
    }
}
