package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.tokonow.ChatTokoNowWarehouseResponse
import com.tokopedia.topchat.common.data.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class ChatTokoNowWarehouseUseCase @Inject constructor(
    private val gqlUseCase: GraphqlUseCase<ChatTokoNowWarehouseResponse>
) {

    fun getWarehouseId(
        msgId: String
    ) = flow {
        emit(Resource.loading(null))
        val param = generateParam(msgId)
        val response = gqlUseCase.apply {
            setGraphqlQuery(query)
            setRequestParams(param)
            setTypeClass(ChatTokoNowWarehouseResponse::class.java)
        }.executeOnBackground()
        emit(Resource.success(response))
    }

    private fun generateParam(msgId: String): Map<String, Any?> {
        return mapOf(
            paramMsgId to msgId
        )
    }

    private val query = """
        query chatTokoNowWarehouse($$paramMsgId: String!) {
          chatTokoNowWarehouse(msgID:$$paramMsgId) {
            warehouseId
          }
        }
    """.trimIndent()

    companion object {
        private const val paramMsgId = "msgID"
    }
}