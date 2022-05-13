package com.tokopedia.chatbot.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.R
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import kotlinx.coroutines.withContext
import rx.Subscriber
import java.lang.NumberFormatException
import javax.inject.Inject

/**
 * @author : Steven 30/11/18
 */

class GetExistingChatUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val dispatcher: CoroutineDispatchers,
) {

    var minReplyTime = "" // for param beforeReplyTime for top
    var maxReplyTime = "" // for param afterReplyTime for bottom
    var hasNext = false // has next top
    var hasNextAfter = false // has next bottom

    suspend fun getFirstPageChat(messageId: String): GetExistingChatPojo {
        return withContext(dispatcher.io) {
            val topQuery = generateFirstPageQuery()
            val params = generateFirstPageParam(messageId)
            val response = getChat(topQuery, params)
            updateMinReplyTime(response)
            updateMaxReplyTime(response)
            response
        }
    }

    private fun generateFirstPageQuery(): String {
        return if (minReplyTime.isNotEmpty()) {
            generateTopQuery()
        } else {
            requestQuery.format("", "")
        }
    }

    private fun generateFirstPageParam(messageId: String): Map<String, Any> {
        return mutableMapOf<String, Any>(
            PARAM_MESSAGE_ID to messageId.toLongOrZero()
        ).apply {
            if (minReplyTime.isNotEmpty()) {
                put(PARAM_BEFORE_REPLY_TIME, minReplyTime)
            }
        }
    }

    suspend fun getBottomChat(messageId: String): GetExistingChatPojo {
        return withContext(dispatcher.io) {
            val topQuery = generateBottomQuery()
            val params = generateBottomParam(messageId)
            val response = getChat(topQuery, params)
            updateMaxReplyTime(response)
            response
        }
    }

    private fun generateBottomQuery(): String {
        return requestQuery.format(
            ", $${PARAM_AFTER_REPLY_TIME}: String",
            ", afterReplyTime: $${PARAM_AFTER_REPLY_TIME}"
        )
    }

    private fun generateBottomParam(messageId: String): Map<String, Any> {
        return mapOf(
            PARAM_MESSAGE_ID to messageId.toLongOrZero(),
            PARAM_AFTER_REPLY_TIME to maxReplyTime
        )
    }

    suspend fun getTopChat(messageId: String): GetExistingChatPojo {
        return withContext(dispatcher.io) {
            val topQuery = generateTopQuery()
            val params = generateTopParam(messageId)
            val response = getChat(topQuery, params)
            updateMinReplyTime(response)
            response
        }
    }

    private fun generateTopQuery(): String {
        return requestQuery.format(
            ", $${PARAM_BEFORE_REPLY_TIME}: String",
            ", beforeReplyTime: $${PARAM_BEFORE_REPLY_TIME}"
        )
    }

    private fun updateMinReplyTime(chat: GetExistingChatPojo) {
        minReplyTime = chat.chatReplies.minReplyTime
        hasNext = chat.chatReplies.hasNext
    }

    fun updateMinReplyTime(replyTime : String){
        minReplyTime = replyTime
    }

    private fun updateMaxReplyTime(chat: GetExistingChatPojo) {
        maxReplyTime = chat.chatReplies.maxReplyTime
        hasNextAfter = chat.chatReplies.hasNextAfter
    }

    private suspend fun getChat(query: String, params: Map<String, Any>): GetExistingChatPojo {
        return repository.request(query, params)
    }

    private fun generateTopParam(messageId: String): Map<String, Any> {
        return mapOf(
            PARAM_MESSAGE_ID to messageId.toLongOrZero(),
            PARAM_BEFORE_REPLY_TIME to minReplyTime
        )
    }

    //Don't know the usage of this
    fun isInTheMiddleOfThePage(): Boolean {
        return hasNextAfter
    }

    fun reset() {
        minReplyTime = ""
        maxReplyTime = ""
        hasNext = false
        hasNextAfter = false
    }

    private val requestQuery = """
       query get_chat_replies(${'$'}${PARAM_MESSAGE_ID}: Int!%s) {
          chatReplies(msgId: ${'$'}${PARAM_MESSAGE_ID}, isTextOnly: true%s)  {
            hasNext
            hasNextAfter
            minReplyTime
            maxReplyTime
            textareaReply
            contacts {
              role
              userId
              shopId
              interlocutor
              name
              tag
              thumbnail
              domain
              status {
                timestamp
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
                  parentReply {
                     attachmentID
                     attachmentType
                        senderID
                        name
                        replyID
                        replyTimeUnixNano
                        fraudStatus
                        source
                        mainText
                        subText
                        imageURL
                        isExpired
                        localID
                  }
                }
              }
            }
          }
        }

    """.trimIndent()

    companion object {
        const val PARAM_MESSAGE_ID: String = "msgId"
        const val PARAM_BEFORE_REPLY_TIME: String = "beforeReplyTime"
        const val PARAM_AFTER_REPLY_TIME: String = "afterReplyTime"
    }

}