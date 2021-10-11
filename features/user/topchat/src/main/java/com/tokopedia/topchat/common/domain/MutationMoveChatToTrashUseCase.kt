package com.tokopedia.topchat.common.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import javax.inject.Inject

open class MutationMoveChatToTrashUseCase @Inject constructor(
    private val repository: GraphqlRepository
) {

    suspend fun execute(messageId: String): ChatDeleteStatus {
        val request = GraphqlRequest(query, ChatDeleteStatus::class.java, createParams(messageId))
        val response = repository.response(listOf(request))
        val error = response.getError(ChatDeleteStatus::class.java)

        if (error == null || error.isEmpty()) {
            return response.getData(ChatDeleteStatus::class.java) as ChatDeleteStatus
        } else {
            throw MessageErrorException(
                error.mapNotNull {
                    it.message
                }.joinToString(separator = ", ")
            )
        }
    }

    companion object {
        private val query = """
            mutation deleteChat(${'$'}messageId: Int!) {
             chatMoveToTrash(ids:[${'$'}messageId]) {
               list{
                 IsSuccess
                 DetailResponse
                 MsgID
               }
             }
            }
        """.trimIndent()

        fun createParams(messageId: String): Map<String, Any> {
            return mapOf(ChatListQueriesConstant.PARAM_MESSAGE_ID to messageId)
        }
    }

}