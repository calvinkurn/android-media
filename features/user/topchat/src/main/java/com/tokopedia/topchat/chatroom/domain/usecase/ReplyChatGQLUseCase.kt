package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

open class ReplyChatGQLUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatReplyPojo>
) {

    suspend fun replyMessage(
            msgId: String,
            msg: String,
            filePath: String,
            source: String
    ): ChatReplyPojo {
        val params = generateParam(msgId, msg, filePath, source)
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
    ): Map<String, Any> {
        val requestParams = ArrayMap<String, Any>()
        requestParams[PARAM_MSG_ID] = msgId
        requestParams[PARAM_MSG] = msg
        requestParams[PARAM_ATTACHMENT_TYPE] = 2
        requestParams[PARAM_FILE_PATH] = filePath
        requestParams[PARAM_SOURCE] = source
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

        private const val query = "mutation ($$PARAM_MSG_ID:String!, $$PARAM_MSG:String!, $$PARAM_ATTACHMENT_TYPE:Int!, $$PARAM_FILE_PATH:String!, $$PARAM_SOURCE:String!)" +
                " {\n" +
                "  chatReplyChat(msgID: $$PARAM_MSG_ID, msg: $$PARAM_MSG, attachmentType: $$PARAM_ATTACHMENT_TYPE, filePath: $$PARAM_FILE_PATH, source: $$PARAM_SOURCE) {\n" +
                "    msgID\n" +
                "    senderID\n" +
                "    msg\n" +
                "    replyTime\n" +
                "    from\n" +
                "    role\n" +
                "    attachment {\n" +
                "      id\n" +
                "      type\n" +
                "      fallback {\n" +
                "        html\n" +
                "        message\n" +
                "      }\n" +
                "      attributes\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }

}