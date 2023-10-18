package com.tokopedia.topchat.chatlist.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import javax.inject.Inject

class GetChatNotificationUseCase @Inject constructor(
    @ApplicationContext private val graphql: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, NotificationsPojo>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        query get_chat_notif($$PARAM_SHOP_ID: String) {
          notifications(input: {shop_id: $$PARAM_SHOP_ID}) {
            chat {
              unreadsSeller
              unreadsUser
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): NotificationsPojo {
        val i = mapOf(PARAM_SHOP_ID to params)
        return graphql.request(graphqlQuery(), i)
    }

    companion object {
        private const val PARAM_SHOP_ID = "shop_id"
    }
}
