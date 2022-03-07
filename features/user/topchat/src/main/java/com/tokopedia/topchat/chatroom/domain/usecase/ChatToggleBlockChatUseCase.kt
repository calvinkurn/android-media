package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import javax.inject.Inject

open class ChatToggleBlockChatUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<ChatToggleBlockChatUseCase.Param, ChatSettingsResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        mutation chatToggleBlockChat($$PARAM_MSG_ID: String!, $$PARAM_BLOCK_TYPE: String!, $$PARAM_IS_BLOCKED: Boolean!){
          chatToggleBlockChat(messageID:$$PARAM_MSG_ID, blockType: $$PARAM_BLOCK_TYPE, isBlocked: $$PARAM_IS_BLOCKED){
        		success
        		block_status{
        			is_blocked
        			is_promo_blocked
        			blocked_until
        		}
        	}
        }
    """.trimIndent()

    override suspend fun execute(params: Param): ChatSettingsResponse {
        return repository.request(graphqlQuery(), params)
    }

    data class Param(
        @SerializedName(PARAM_MSG_ID)
        var msgId: String = "",
        @SerializedName(PARAM_BLOCK_TYPE)
        var blockType: String = BlockType.Personal.value,
        @SerializedName(PARAM_IS_BLOCKED)
        var isBlocked: Boolean = false
    ): GqlParam

    enum class BlockType(val value: String) {
        Personal("1"),
        Promo("2")
    }

    companion object {
        const val PARAM_MSG_ID = "messageID"
        const val PARAM_BLOCK_TYPE = "blockType"
        const val PARAM_IS_BLOCKED = "isBlocked"
    }
}