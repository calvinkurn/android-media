package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.common.data.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class SmartReplyQuestionUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatSmartReplyQuestionResponse>
) {

    fun getSrwList(
            msgId: String
    ) = flow {
        emit(Resource.loading(null))
        val param = generateParam(msgId)
        val response = gqlUseCase.apply {
            setGraphqlQuery(query)
            setRequestParams(param)
            setTypeClass(ChatSmartReplyQuestionResponse::class.java)
        }.executeOnBackground()
        emit(Resource.success(response))
    }

    private fun generateParam(msgId: String): Map<String, Any?> {
        return mapOf(
                paramMsgId to msgId
        )
    }

    private val query = """
        query chatSmartReplyQuestion($$paramMsgId: String!){
          chatSmartReplyQuestion(msgID: $$paramMsgId){
            isSuccess
            hasQuestion
            title
            list {
              content
              intent
            }
          }
        }
    """.trimIndent()

    companion object {
        private const val paramMsgId = "msgID"
    }
}