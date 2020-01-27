package com.tokopedia.topchat.chatroom.domain.usecase

import android.util.Log
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import javax.inject.Inject

class GetChatRoomSettingUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<RoomSettingResponse>
) {

    val paramMsgId = "msgId"

    fun execute(msgId: String) {
        val params = generateParams(msgId)
        gqlUseCase.apply {
            setTypeClass(RoomSettingResponse::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
            execute({ result ->
                val topWidget: List<Visitable<Any>> = filterSettingToBeShown(result)
                Log.d("ASD", result.toString())
            }, { })
        }
    }

    private fun filterSettingToBeShown(result: RoomSettingResponse): List<Visitable<Any>> {
        return listOf()
    }

    private fun generateParams(msgId: String): Map<String, Any> {
        return mapOf(
                paramMsgId to msgId
        )
    }

    private val query = """
        query getChatRoomSettings($$paramMsgId: String!){
          chatRoomSettings(msgId: $$paramMsgId) {
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
}