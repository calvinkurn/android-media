package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.TopChatWebSocketParam.generateParentReplyRequestPayload
import javax.inject.Inject

open class ReplyChatGQLUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatReplyPojo>
) {

    suspend fun replyMessage(
            msgId: String,
            msg: String,
            filePath: String,
            source: String,
            parentReply: ParentReply? = null
    ): ChatReplyPojo {
        val params = generateParam(msgId, msg, filePath, source, parentReply)
        return gqlUseCase.apply {
            setTypeClass(ChatReplyPojo::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
        }.executeOnBackground()
    }

    private fun generateParam(
        msgId: String,
        msg: String,
        filePath: String,
        source: String,
        parentReply: ParentReply?
    ): Map<String, Any> {
        val requestParams = ArrayMap<String, Any>()
        requestParams[PARAM_MSG_ID] = msgId
        requestParams[PARAM_MSG] = msg
        requestParams[PARAM_ATTACHMENT_TYPE] = 2
        requestParams[PARAM_FILE_PATH] = filePath
        requestParams[PARAM_SOURCE] = source
        val parentReplyStr = generateParentReplyRequestPayload(parentReply)?.toString() ?: ""
        requestParams[PARAM_PARENT_REPLY] = parentReplyStr
        return requestParams
    }

    fun cancel() {
        gqlUseCase.cancelJobs()
    }

    companion object {
        private const val PARAM_MSG_ID = "msgID"
        private const val PARAM_MSG = "msg"
        private const val PARAM_ATTACHMENT_TYPE = "attachmentType"
        private const val PARAM_FILE_PATH = "filePath"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_PARENT_REPLY = "parentReply"

        private val query = """
            mutation chatReplyChat( 
                    $$PARAM_MSG_ID:String!,  
                    $$PARAM_MSG:String!, 
                    $$PARAM_ATTACHMENT_TYPE:Int!, 
                    $$PARAM_FILE_PATH:String!, 
                    $$PARAM_SOURCE:String!, 
                    $$PARAM_PARENT_REPLY:String! 
                ) { 
                  chatReplyChat(
                      msgID: $$PARAM_MSG_ID,
                      msg: $$PARAM_MSG, 
                      attachmentType: $$PARAM_ATTACHMENT_TYPE, 
                      filePath: $$PARAM_FILE_PATH, 
                      source: $$PARAM_SOURCE,
                      parentReply: $$PARAM_PARENT_REPLY
                  ) { 
                    msgID 
                    senderID 
                    msg 
                    replyTime 
                    from 
                    role 
                    attachment { 
                      id 
                      type 
                      fallback { 
                        html 
                        message 
                      } 
                      attributes 
                    } 
                  } 
                }
        """.trimIndent()
    }

}