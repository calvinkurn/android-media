package com.tokopedia.topchat.common.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import javax.inject.Inject

open class MutationMoveChatToTrashUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<String, ChatDeleteStatus>(dispatcher.io) {

    override fun graphqlQuery(): String = """
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

    override suspend fun execute(params: String): ChatDeleteStatus {
        val param = createParams(params)
        return repository.request(graphqlQuery(), param)
    }

    private fun createParams(messageId: String): Map<String, Any> {
        return mapOf(PARAM_MESSAGE_ID to messageId)
    }

    companion object {
        private const val PARAM_MESSAGE_ID = "messageId"
    }
}