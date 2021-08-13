package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import javax.inject.Inject

open class GetChatRoomSettingUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<RoomSettingResponse>
) {

    val paramMsgId = "msgId"

    fun execute(
            msgId: String,
            onSuccess: (List<Visitable<TopChatTypeFactory>>) -> Unit
    ) {
        val params = generateParams(msgId)
        gqlUseCase.apply {
            setTypeClass(RoomSettingResponse::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
            execute({ result ->
                val topWidget: List<Visitable<TopChatTypeFactory>> = filterSettingToBeShown(result)
                onSuccess(topWidget)
            }, { })
        }
    }

    private fun filterSettingToBeShown(result: RoomSettingResponse): List<Visitable<TopChatTypeFactory>> {
        val widgets = arrayListOf<Visitable<TopChatTypeFactory>>()

        if (result.showBanner) {
            widgets.add(result.roomBanner)
        }

        if (result.showFraudAlert) {
            widgets.add(result.fraudAlert)
        }

        return widgets
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