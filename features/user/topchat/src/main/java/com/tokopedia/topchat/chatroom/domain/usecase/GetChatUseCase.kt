package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * @author : Steven 30/11/18
 */

class GetChatUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetExistingChatPojo>,
        private val mapper: TopChatRoomGetExistingChatMapper,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    var minReplyTime = "-1" // for param beforeReplyTime for top
    var maxReplyTime = "-1" // for param afterReplyTime for bottom
    var hasNext = false // has next top
    var hasNextAfter = false // has next bottom

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun getFirstPageChat(
            messageId: String,
            onSuccess: (ChatroomViewModel) -> Unit,
            onErrorGetChat: (Throwable) -> Unit
    ) {
        val topQuery = generateFirstPageQuery()
        val params = generateFirstPageParam(messageId)
        getChat(topQuery, params, onSuccess, onErrorGetChat) { chat, _ ->
            updateMinReplyTime(chat)
        }
    }

    fun getTopChat(
            messageId: String,
            onSuccess: (ChatroomViewModel) -> Unit,
            onErrorGetChat: (Throwable) -> Unit
    ) {
        val topQuery = generateTopQuery()
        val params = generateTopParam(messageId)
        getChat(topQuery, params, onSuccess, onErrorGetChat) { chat, _ ->
            updateMinReplyTime(chat)
        }
    }

    fun getBottomChat(
            messageId: String,
            onSuccess: (ChatroomViewModel) -> Unit,
            onErrorGetChat: (Throwable) -> Unit
    ) {
        val topQuery = generateBottomQuery()
        val params = generateBottomParam(messageId)
        getChat(topQuery, params, onSuccess, onErrorGetChat) { chat, _ ->
            updateMaxReplyTime(chat)
        }
    }

    private fun getChat(
            query: String,
            params: Map<String, Any>,
            onSuccess: (ChatroomViewModel) -> Unit,
            onErrorGetChat: (Throwable) -> Unit,
            onResponseReady: (GetExistingChatPojo, ChatroomViewModel) -> Unit
    ) {
        launchCatchError(
                dispatchers.IO,
                {
                    val response = gqlUseCase.apply {
                        setTypeClass(GetExistingChatPojo::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val chatroomViewModel = mapper.map(response)
                    onResponseReady(response, chatroomViewModel)
                    withContext(dispatchers.Main) {
                        onSuccess(chatroomViewModel)
                    }
                },
                {
                    withContext(dispatchers.Main) {
                        onErrorGetChat(it)
                    }
                }
        )
    }

    private fun generateFirstPageParam(messageId: String): Map<String, Any> {
        return mapOf(
                PARAM_MESSAGE_ID to messageId.toInt()
        )
    }

    private fun generateBottomParam(messageId: String): Map<String, Any> {
        return mapOf(
                PARAM_MESSAGE_ID to messageId.toInt(),
                PARAM_AFTER_REPLY_TIME to maxReplyTime
        )
    }

    private fun generateFirstPageQuery(): String {
        return requestQuery.format("", "")
    }

    private fun generateBottomQuery(): String {
        return requestQuery.format(
                ", $$PARAM_AFTER_REPLY_TIME: String",
                ", afterReplyTime: $$PARAM_AFTER_REPLY_TIME"
        )
    }

    private fun generateTopQuery(): String {
        return requestQuery.format(
                ", $$PARAM_BEFORE_REPLY_TIME: String",
                ", beforeReplyTime: $$PARAM_BEFORE_REPLY_TIME"
        )
    }

    private fun updateMinReplyTime(chat: GetExistingChatPojo) {
        minReplyTime = chat.chatReplies.minReplyTime
        hasNext = chat.chatReplies.hasNext
    }

    private fun updateMaxReplyTime(chat: GetExistingChatPojo) {
        maxReplyTime = chat.chatReplies.maxReplyTime
        hasNextAfter = chat.chatReplies.hasNextAfter
    }

    private fun generateTopParam(messageId: String): Map<String, Any> {
        return mapOf(
                PARAM_MESSAGE_ID to messageId.toInt(),
                PARAM_BEFORE_REPLY_TIME to minReplyTime
        )
    }

    fun unsubscribe() {
        if (coroutineContext.isActive) {
            cancel()
        }
    }

    companion object {
        private const val PARAM_MESSAGE_ID: String = "msgId"
        private const val PARAM_BEFORE_REPLY_TIME: String = "beforeReplyTime"
        private const val PARAM_AFTER_REPLY_TIME: String = "afterReplyTime"
    }

    private val requestQuery = """
        query get_chat_replies($$PARAM_MESSAGE_ID: Int!%s) {
          status
          chatReplies(msgId: $$PARAM_MESSAGE_ID, isTextOnly: true%s) {
            minReplyTime
            block {
              blockedUntil
              isBlocked
              isPromoBlocked
            }
            hasNext
            textareaReply
            attachmentIDs
            contacts {
              role
              userId
              shopId
              interlocutor
              name
              tag
              thumbnail
              domain
              isOfficial
              isGold
              badge
              status {
                timestamp
                timestampStr
                isOnline
              }
            }
            list {
              date
              chats {
                time
                replies {
                  msgId
                  replyId
                  senderId
                  senderName
                  role
                  msg
                  replyTime
                  status
                  attachmentID
                  isOpposite
                  isHighlight
                  isRead
                  blastId
                  source
                  attachment {
                    id
                    type
                    attributes
                    fallback {
                      message
                      html
                    }
                  }
                }
              }
            }
          }
        }

    """.trimIndent()

}