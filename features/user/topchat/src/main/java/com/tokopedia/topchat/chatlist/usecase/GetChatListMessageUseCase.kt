package com.tokopedia.topchat.chatlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class GetChatListMessageUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatListPojo>,
        private val mapper: GetChatListMessageMapper,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    var hasNext = false
    var job: Job? = null

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    open fun getChatList(
            page: Int,
            filter: String,
            tab: String,
            onSuccess: (ChatListPojo, List<String>, List<String>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        job = launchCatchError(dispatchers.io,
                {
                    val params = generateParams(page, filter, tab)
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatListPojo::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    hasNext = response.data.hasNext
                    mapper.convertStrTimestampToLong(response)
                    val pinnedChatMsgId = mapper.mapPinChat(response, page)
                    val unPinnedChatMsgId = mapper.mapUnpinChat(response)
                    withContext(dispatchers.main) {
                        onSuccess(response, pinnedChatMsgId, unPinnedChatMsgId)
                    }
                },
                {
                    withContext(dispatchers.main) {
                        onError(it)
                    }
                }
        )
    }

    private fun generateParams(
            page: Int,
            filter: String,
            tab: String
    ): Map<String, Any> {
        return mapOf(
                paramPage to page,
                paramFilter to filter,
                paramTab to tab
        )
    }

    fun reset() {
        hasNext = false
    }

    fun cancelRunningOperation() {
        job?.cancel()
    }

    val query = """
        query get_existing_message_id($$paramPage: Int!, $$paramFilter: String!, $$paramTab: String!) {
          status
          chatListMessage(page: $$paramPage, filter: $$paramFilter, tab: $$paramTab) {
            list{
                  msgID
                  messageKey
                  attributes {
                    contact {
                      id
                      role
                      domain
                      name
                      shopStatus
                      tag
                      thumbnail
                    }
                    lastReplyMessage
                    lastReplyTimeStr
                    readStatus
                    unreads
                    unreadsreply
                    fraudStatus
                    pinStatus
                    isReplyByTopbot
                    label
                  }
                }
            hasNext
            pagingNext
            showTimeMachine
          }
        }

    """.trimIndent()

    companion object {
        const val paramPage = "page"
        const val paramFilter = "filter"
        const val paramTab = "tab"
    }
}