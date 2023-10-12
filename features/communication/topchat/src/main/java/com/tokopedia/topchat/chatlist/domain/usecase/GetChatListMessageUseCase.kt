package com.tokopedia.topchat.chatlist.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListParam
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListResponse
import javax.inject.Inject

open class GetChatListMessageUseCase @Inject constructor(
    private val repo: GraphqlRepository,
    private val mapper: GetChatListMessageMapper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ChatListParam, ChatListResponse>(dispatchers.io) {

    var hasNext = false
    override suspend fun execute(params: ChatListParam): ChatListResponse {
        val p = generateParams(params)
        val response: ChatListPojo = repo.request(graphqlQuery(), p)
        hasNext = response.data.hasNext
        mapper.convertStrTimestampToLong(response)
        val pinnedChatMsgId = mapper.mapPinChat(response, params.page)
        val unPinnedChatMsgId = mapper.mapUnpinChat(response)
        return ChatListResponse(response, pinnedChatMsgId, unPinnedChatMsgId)
    }

    override fun graphqlQuery(): String = """
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
                labelIconURL
              }
            }
            hasNext
            pagingNext
            showTimeMachine
          }
        }
    """.trimIndent()

    private fun generateParams(input: ChatListParam): Map<String, Any> {
        return mapOf(
            paramPage to input.page,
            paramFilter to input.filter,
            paramTab to input.tab
        )
    }

    fun reset() {
        hasNext = false
    }

    companion object {
        const val paramPage = "page"
        const val paramFilter = "filter"
        const val paramTab = "tab"
    }
}
