package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.requestAsFlow
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.tokonow.ChatTokoNowWarehouseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class GetChatTokoNowWarehouseUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
): FlowUseCase<String, ChatTokoNowWarehouseResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query chatTokoNowWarehouse($$PARAM_MSG_ID: String!) {
          chatTokoNowWarehouse(msgID:$$PARAM_MSG_ID) {
            warehouseId
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): Flow<ChatTokoNowWarehouseResponse>{
        val param = generateParam(params)
        return repository.requestAsFlow(graphqlQuery(), param)
    }

    private fun generateParam(msgId: String): Map<String, Any> {
        return mapOf(
            PARAM_MSG_ID to msgId
        )
    }

    companion object {
        private const val PARAM_MSG_ID = "msgID"
    }

}