package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatBlockResponse
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatBlockStatus
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatToggleBlockChatUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class ChatToggleBlockChatUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatchers: CoroutineDispatchers
): ChatToggleBlockChatUseCase(repository, dispatchers) {

    var isBlocked = false
    var isPromoBlocked = false

    var response = createResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(response::class.java, "Oops!")
            }
            field = value
        }

    fun createResponse(
        isBlocked: Boolean = false,
        isPromoBlocked: Boolean = false,
        blockedUntil: String = "2022-07-19T13:24:40.7284+07:00"
    ): ChatSettingsResponse {
        return ChatSettingsResponse(
            ChatBlockResponse(
                true, ChatBlockStatus(isBlocked, isPromoBlocked, blockedUntil)
            )
        )
    }
}