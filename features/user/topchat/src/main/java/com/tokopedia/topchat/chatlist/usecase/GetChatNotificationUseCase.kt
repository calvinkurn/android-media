package com.tokopedia.topchat.chatlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatlist.pojo.NotificationsPojo
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class GetChatNotificationUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<NotificationsPojo>,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    open fun getChatNotification(
            onSuccess: (NotificationsPojo) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.IO,
                {
                    val response = gqlUseCase.apply {
                        setTypeClass(NotificationsPojo::class.java)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.Main) {
                        onSuccess(response)
                    }
                },
                {
                    withContext(dispatchers.Main) {
                        onError(it)
                    }
                }
        )
    }

    private val query = """
        query get_chat_notif() {
          notifications {
              chat {
                unreadsSeller
                unreadsUser
              }
            }
        }
    """.trimIndent()
}