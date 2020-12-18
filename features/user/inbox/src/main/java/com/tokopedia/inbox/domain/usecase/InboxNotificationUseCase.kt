package com.tokopedia.inbox.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.inbox.common.InboxCoroutineContextProvider
import com.tokopedia.inbox.domain.data.notification.InboxCounter
import com.tokopedia.inbox.domain.data.notification.InboxNotificationResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class InboxNotificationUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<InboxNotificationResponse>,
        private val dispatchers: InboxCoroutineContextProvider
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun getNotification(
            onSuccess: (InboxCounter) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(
                dispatchers.IO,
                {
                    val response = gqlUseCase.apply {
                        setTypeClass(InboxNotificationResponse::class.java)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.Main) {
                        onSuccess(response.notifications.inboxCounter)
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
        query notifications_inbox_counter {
          notifications{
            chat{
              unreads
              unreadsSeller
              unreadsUser
            }
            inbox_counter{
              all{
                total_int
                notifcenter_int
                chat_int
                talk_int
              }
              buyer{
                total_int
                notifcenter_int
                chat_int
                talk_int
              }
              seller{
                total_int
                notifcenter_int
                chat_int
                talk_int
              }
            }
          }
        }
    """.trimIndent()
}