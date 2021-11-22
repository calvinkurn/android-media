package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import javax.inject.Inject

open class GetChatRoomSettingUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<String, RoomSettingResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query getChatRoomSettings($$PARAM_MSG_ID: String!){
          chatRoomSettings(msgId: $$PARAM_MSG_ID) {
            banner {
            typeString
              text
              imageUrl
              enable
            }
            fraudAlert {
              text
              imageUrl
              enable
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): RoomSettingResponse {
        return repository.request(graphqlQuery(), mapOf(PARAM_MSG_ID to params))
    }

    companion object {
        private const val PARAM_MSG_ID = "msgId"
    }

}