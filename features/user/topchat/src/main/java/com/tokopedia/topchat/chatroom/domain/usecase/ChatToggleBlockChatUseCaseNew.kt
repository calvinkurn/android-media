package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.pojo.param.ToggleBlockChatParam
import com.tokopedia.topchat.chatroom.domain.pojo.param.ToggleBlockChatParam.Companion.PARAM_BLOCK_TYPE
import com.tokopedia.topchat.chatroom.domain.pojo.param.ToggleBlockChatParam.Companion.PARAM_IS_BLOCKED
import com.tokopedia.topchat.chatroom.domain.pojo.param.ToggleBlockChatParam.Companion.PARAM_MSG_ID
import javax.inject.Inject

class ChatToggleBlockChatUseCaseNew @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<ToggleBlockChatParam, ChatSettingsResponse>(dispatcher.io) {

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

    override suspend fun execute(params: ToggleBlockChatParam): ChatSettingsResponse {
        return repository.request(graphqlQuery(), params)
    }

}