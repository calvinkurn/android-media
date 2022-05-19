package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.TopChatWebSocketParam.generateParentReplyRequestPayload
import kotlinx.coroutines.cancelChildren
import javax.inject.Inject

open class ReplyChatGQLUseCase @Inject constructor(
        private val repository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers
): CoroutineUseCase<ReplyChatGQLUseCase.Param, ChatReplyPojo>(dispatcher.io) {

    override fun graphqlQuery(): String = """
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

    override suspend fun execute(params: Param): ChatReplyPojo {
        val replyParam = generateParam(params)
        return repository.request(graphqlQuery(), replyParam)
    }

    private fun generateParam(param: ReplyChatGQLUseCase.Param): Map<String, Any> {
        val requestParams = ArrayMap<String, Any>()
        requestParams[PARAM_MSG_ID] = param.msgId
        requestParams[PARAM_MSG] = param.msg
        //currently ReplyChatGQL only support attach image
        if (param.filePath != null) {
            requestParams[PARAM_ATTACHMENT_TYPE] = 2
            requestParams[PARAM_FILE_PATH] = param.filePath
        }
        requestParams[PARAM_SOURCE] = param.source
        val parentReplyStr = generateParentReplyRequestPayload(param.parentReply)?.toString() ?: ""
        requestParams[PARAM_PARENT_REPLY] = parentReplyStr
        return requestParams
    }

    fun cancel() {
        dispatcher.io.cancelChildren()
    }

    class Param(
        var msgId: String = "",
        var msg: String = "",
        var filePath: String? = null,
        var source: String = "",
        var parentReply: ParentReply? = null
    ): GqlParam

    companion object {
        private const val PARAM_MSG_ID = "msgID"
        private const val PARAM_MSG = "msg"
        private const val PARAM_ATTACHMENT_TYPE = "attachmentType"
        private const val PARAM_FILE_PATH = "filePath"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_PARENT_REPLY = "parentReply"
    }
}