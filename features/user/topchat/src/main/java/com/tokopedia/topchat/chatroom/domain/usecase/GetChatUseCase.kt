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

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun getChat(
            messageId: String,
            beforeReplyTime: String,
            onSuccess: (ChatroomViewModel) -> Unit,
            onErrorGetChat: (Throwable) -> Unit
    ) {
        launchCatchError(
                dispatchers.IO,
                {
                    val params = generateParam(messageId, beforeReplyTime)
                    val response = gqlUseCase.apply {
                        setTypeClass(GetExistingChatPojo::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val chatroomViewModel = mapper.map(response)
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

    fun generateParam(messageId: String, beforeReplyTime: String): Map<String, Any> {
        val intMessageId = if (messageId.isNotBlank()) messageId.toInt() else 0
        return mapOf(
                PARAM_MESSAGE_ID to intMessageId,
                PARAM_BEFORE_REPLY_TIME to beforeReplyTime
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
    }

    private val query = """
        query get_chat_replies($$PARAM_MESSAGE_ID: Int!, $$PARAM_BEFORE_REPLY_TIME: String) {
          status
          chatReplies(msgId: $$PARAM_MESSAGE_ID, beforeReplyTime: $$PARAM_BEFORE_REPLY_TIME, isTextOnly: true) {
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