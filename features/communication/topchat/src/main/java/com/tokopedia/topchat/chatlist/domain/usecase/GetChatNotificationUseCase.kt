package com.tokopedia.topchat.chatlist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatlist.domain.pojo.param.NotificationParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class GetChatNotificationUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<NotificationsPojo>,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    open fun getChatNotification(
            shopId: String,
            onSuccess: (NotificationsPojo) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.io,
                {
                    val param = getParams(shopId)
                    val response = gqlUseCase.apply {
                        setTypeClass(NotificationsPojo::class.java)
                        setGraphqlQuery(query)
                        setRequestParams(param)
                    }.executeOnBackground()
                    withContext(dispatchers.main) {
                        onSuccess(response)
                    }
                },
                {
                    withContext(dispatchers.main) {
                        onError(it)
                    }
                }
        )
    }

    private fun getParams(shopId: String): Map<String, Any?> {
        return mapOf(
            PARAM_INPUT to NotificationParam(shopId)
        )
    }

    private val query = """
        query get_chat_notif($$PARAM_INPUT: NotificationRequest) {
          notifications(input: $$PARAM_INPUT){
              chat {
                unreadsSeller
                unreadsUser
              }
            }
        }
    """.trimIndent()

    companion object {
        private const val PARAM_INPUT = "input"
    }
}