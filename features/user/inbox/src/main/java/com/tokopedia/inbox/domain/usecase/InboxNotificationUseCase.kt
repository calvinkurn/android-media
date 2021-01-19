package com.tokopedia.inbox.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.inbox.common.InboxCoroutineContextProvider
import com.tokopedia.inbox.domain.data.notification.InboxNotificationResponse
import com.tokopedia.inbox.domain.data.notification.Notifications
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
            onSuccess: (Notifications) -> Unit,
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
                        onSuccess(response.notifications)
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
            total_cart
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
              seller_discussion_unresponded {
                total_int
              }
            }
          }
        }
    """.trimIndent()
}