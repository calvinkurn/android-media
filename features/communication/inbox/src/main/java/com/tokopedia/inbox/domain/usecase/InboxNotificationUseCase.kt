package com.tokopedia.inbox.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers 
import com.tokopedia.inbox.domain.data.notification.InboxNotificationResponse
import com.tokopedia.inbox.domain.data.notification.Notifications
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatlist.domain.pojo.param.NotificationParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class InboxNotificationUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<InboxNotificationResponse>,
        private val dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun getNotification(
            shopId: String,
            onSuccess: (Notifications) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(
                block = {
                    val param = getParams(shopId)
                    val response = gqlUseCase.apply {
                        setTypeClass(InboxNotificationResponse::class.java)
                        setRequestParams(param)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    onSuccess(response.notifications)
                },
                onError = {
                    onError(it)
                }
        )
    }

    private fun getParams(shopId: String): Map<String, Any?> {
        return mapOf(
            PARAM_INPUT to NotificationParam(shopId)
        )
    }

    private val query = """
        query notifications_inbox_counter($$PARAM_INPUT: NotificationRequest) {
          notifications(input: $$PARAM_INPUT){
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
                review_int
              }
              buyer{
                total_int
                notifcenter_int
                chat_int
                talk_int
                review_int
              }
              seller{
                total_int
                notifcenter_int
                chat_int
                talk_int
                review_int
              }
            }
          }
        }
    """.trimIndent()

    companion object {
        private const val PARAM_INPUT = "input"
    }
}