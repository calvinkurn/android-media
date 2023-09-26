package com.tokopedia.chatbot.chatbot2.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author : Steven 30/11/18
 */

class GetExistingChatUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val dispatcher: CoroutineDispatchers
) {

    var minReplyTime = "" // for param beforeReplyTime for top
    var maxReplyTime = "" // for param afterReplyTime for bottom
    var hasNext = false // has next top
    var hasNextAfter = false // has next bottom

    suspend fun getFirstPageChat(messageId: String): GetExistingChatPojo {
        return withContext(dispatcher.io) {
            val topQuery = generateFirstPageQuery()
            val params = generateFirstPageParam(messageId)
//            val response = getChat(topQuery, params)
            val response = Gson().fromJson(RESPONSE, GetExistingChatPojo::class.java)
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
            ", $$PARAM_AFTER_REPLY_TIME: String",
            ", afterReplyTime: $$PARAM_AFTER_REPLY_TIME"
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
            updateMaxReplyTime(response)
            response
        }
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

    fun updateMinReplyTime(replyTime: String) {
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

    fun reset() {
        minReplyTime = ""
        maxReplyTime = ""
        hasNext = false
        hasNextAfter = false
    }

    private val requestQuery = """
       query get_chat_replies(${'$'}$PARAM_MESSAGE_ID: Int!%s) {
          chatReplies(msgId: ${'$'}$PARAM_MESSAGE_ID, isTextOnly: true%s)  {
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

    val RESPONSE = """
        {"chatReplies":{"hasNext":true,"hasNextAfter":false,"minReplyTime":"1695632517075","maxReplyTime":"1695632591753","textareaReply":1,"contacts":[{"role":"User","userId":5515973,"shopId":0,"interlocutor":true,"name":"Tanya","tag":"Pengguna","thumbnail":"https://images-staging.tokopedia.net/img/cache/300/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png","domain":"","status":{"timestamp":1693041354}},{"role":"User","userId":9104447,"shopId":0,"interlocutor":false,"name":"safwan","tag":"Pengguna","thumbnail":"https://images-staging.tokopedia.net/img/cache/300/tPxBYm/2023/1/20/17d1d6b7-50c0-4c06-b16e-3fd60feb70a8.jpg","domain":"","status":{"timestamp":0}}],"list":[{"date":"25 Sep 2023","chats":[{"time":"16:01","replies":[{"msgId":4053344,"replyId":"22413949","senderId":9104447,"senderName":"safwan","role":"User","msg":"Ya","replyTime":"1695632517075296000","status":1,"attachmentID":628484,"isOpposite":false,"isHighlight":false,"isRead":true,"source":"chatbot","attachment":{"id":628484,"type":10,"attributes":"{}","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null},{"msgId":4053344,"replyId":"22413952","senderId":5515973,"senderName":"Tanya","role":"User","msg":"","replyTime":"1695632517152969000","status":1,"attachmentID":628485,"isOpposite":true,"isHighlight":false,"isRead":true,"source":"topbot","attachment":{"id":628485,"type":34,"attributes":"{\"dynamic_attachment\":{\"attribute\":{\"content_code\":101,\"dynamic_content\":\"{\\\"isHidden\\\":false}\",\"render_target\":\"all\",\"user_id\":9104447},\"fallback\":{\"html\":\"\\u003cdiv\\u003eFallback\\u003c/div\\u003e\",\"message\":\"Fallback\"},\"is_log_history\":true}}","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null},{"msgId":4053344,"replyId":"22413954","senderId":5515973,"senderName":"Tanya","role":"User","msg":"Karena sudah bersedia menunggu, untuk mempercepat proses pengecekkan, kamu bisa tuliskan kendala yang dialami dengan singkat dan jelas dalam waktu 15 menit.\u003cbr\u003e\u003cbr\u003eKalau sudah lebih dari 15 menit tidak ada informasi yang kamu berikan, maka percakapan ini akan TANYA akhiri.\u003cbr\u003e","replyTime":"1695632517725617000","status":1,"attachmentID":0,"isOpposite":true,"isHighlight":false,"isRead":true,"source":"chatbot_{\"name\":\"TANYA\",\"icon_url\":\"https://images.tokopedia.net/img/chatbot/tanya.png\",\"icon_url_dark\":\"https://images.tokopedia.net/img/chatbot/tanya-dark.png\"}","attachment":{"id":0,"type":0,"attributes":"","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null}]},{"time":"16:02","replies":[{"msgId":4053344,"replyId":"22413955","senderId":9104447,"senderName":"safwan","role":"User","msg":"batalkan pesanan","replyTime":"1695632528392914000","status":1,"attachmentID":0,"isOpposite":false,"isHighlight":false,"isRead":true,"source":"chatbot","attachment":{"id":0,"type":0,"attributes":"","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null},{"msgId":4053344,"replyId":"22413958","senderId":5515973,"senderName":"Tanya","role":"User","msg":"Mohon tunggu, ya. TANYA akan bantu kamu terhubung dengan CFS Tokopedia Care.","replyTime":"1695632532771853000","status":1,"attachmentID":0,"isOpposite":true,"isHighlight":false,"isRead":true,"source":"chatbot_{\"name\":\"TANYA\",\"icon_url\":\"https://images.tokopedia.net/img/chatbot/tanya.png\",\"icon_url_dark\":\"https://images.tokopedia.net/img/chatbot/tanya-dark.png\"}","attachment":{"id":0,"type":0,"attributes":"","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null},{"msgId":4053344,"replyId":"22413960","senderId":5515973,"senderName":"Tanya","role":"User","msg":"","replyTime":"1695632533331467000","status":1,"attachmentID":628486,"isOpposite":true,"isHighlight":false,"isRead":true,"source":"","attachment":{"id":628486,"type":15,"attributes":"{\"devider\":{\"label\":\"CFS kami akan membalas chat kamu sekitar 10 menit kedepan. Mohon tunggu ya\"}}","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null},{"msgId":4053344,"replyId":"22413962","senderId":5515973,"senderName":"Tanya","role":"User","msg":"Halo, safwan! Sambil menunggu, coba ceritakan kendalamu agar bisa kami proses lebih cepat, ya.","replyTime":"1695632533581395000","status":1,"attachmentID":0,"isOpposite":true,"isHighlight":false,"isRead":true,"source":"chatbot_{\"name\":\"Tokopedia Care\",\"icon_url\":\"https://images.tokopedia.net/img/chatbot/avatar-admin-light.png\",\"icon_url_dark\":\"https://images.tokopedia.net/img/topbot/avatar/avatar-dark-bg.png\"}","attachment":{"id":0,"type":0,"attributes":"","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null}]},{"time":"16:03","replies":[{"msgId":4053344,"replyId":"22413964","senderId":5515973,"senderName":"Tanya","role":"User","msg":"Apakah kamu yakin untuk membatalkan pesanan ini?","replyTime":"1695632586252399000","status":1,"attachmentID":0,"isOpposite":true,"isHighlight":false,"isRead":true,"source":"chatbot_{\"name\":\"Tokopedia Care\",\"icon_url\":\"https://images.tokopedia.net/img/chatbot/avatar-admin-light.png\",\"icon_url_dark\":\"https://images.tokopedia.net/img/topbot/avatar/avatar-dark-bg.png\"}","attachment":{"id":0,"type":0,"attributes":"","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null},{"msgId":4053344,"replyId":"22413966","senderId":5515973,"senderName":"Tanya","role":"User","msg":"","replyTime":"1695632586355514000","status":1,"attachmentID":628487,"isOpposite":true,"isHighlight":false,"isRead":true,"source":"topbot","attachment":{"id":628487,"type":34,"attributes":"{\"dynamic_attachment\":{\"attribute\":{\"content_code\":101,\"dynamic_content\":\"{\\\"isHidden\\\":true}\",\"render_target\":\"all\",\"user_id\":9104447},\"fallback\":{\"html\":\"\\u003cdiv\\u003eMohon segera upgrade version tokopedia anda\\u003c/div\\u003e\",\"message\":\"Mohon segera upgrade version tokopedia anda\"},\"is_log_history\":true}}","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null},{"msgId":4053344,"replyId":"22413968","senderId":5515973,"senderName":"Tanya","role":"User","msg":"Jangan khawatir, TANYA akan bantu solusikan kendalamu. Klik salah satu pilihan kendala yang kamu alami terlebih dulu, ya:","replyTime":"1695632586605008000","status":1,"attachmentID":628488,"isOpposite":true,"isHighlight":false,"isRead":false,"source":"","attachment":{"id":628488,"type":9,"attributes":"{\"new_button_actions\":[{\"action\":\"cancelorder_yes\",\"text\":\"Ya\",\"value\":\"Ya\"},{\"action\":\"cancelorder_no\",\"text\":\"Tidak\",\"value\":\"Tidak\"}],\"button_actions\":[{\"message\":\"Ya\"},{\"message\":\"Tidak\"}],\"is_typing_blocked_on_button_select\":true}","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null},{"msgId":4053344,"replyId":"22413969","senderId":9104447,"senderName":"safwan","role":"User","msg":"Ya","replyTime":"1695632591753108000","status":1,"attachmentID":628489,"isOpposite":false,"isHighlight":false,"isRead":false,"source":"chatbot","attachment":{"id":628489,"type":10,"attributes":"{}","fallback":{"message":"Ya","html":"\u003cdiv\u003eYa\u003c/div\u003e"}},"parentReply":null}]}]}]}}
    """.trimIndent()
}
