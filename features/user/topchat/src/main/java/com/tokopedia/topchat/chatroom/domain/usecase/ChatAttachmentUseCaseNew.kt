package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.param.ChatAttachmentParam
import com.tokopedia.topchat.chatroom.domain.pojo.param.ChatAttachmentParam.Companion.PARAM_ADDRESS_ID
import com.tokopedia.topchat.chatroom.domain.pojo.param.ChatAttachmentParam.Companion.PARAM_DISTRICT_ID
import com.tokopedia.topchat.chatroom.domain.pojo.param.ChatAttachmentParam.Companion.PARAM_LAT_LON
import com.tokopedia.topchat.chatroom.domain.pojo.param.ChatAttachmentParam.Companion.PARAM_MSG_ID
import com.tokopedia.topchat.chatroom.domain.pojo.param.ChatAttachmentParam.Companion.PARAM_POSTAL_CODE
import com.tokopedia.topchat.chatroom.domain.pojo.param.ChatAttachmentParam.Companion.PARAM_REPLY_IDS
import javax.inject.Inject

class ChatAttachmentUseCaseNew @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<ChatAttachmentParam, ChatAttachmentResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query chatAttachments(
            $$PARAM_MSG_ID: Int!, $$PARAM_REPLY_IDS: String, $$PARAM_ADDRESS_ID: Int,
            $$PARAM_DISTRICT_ID: Int, $$PARAM_POSTAL_CODE: String, $$PARAM_LAT_LON: String
        ) {
          chatAttachments(
            msgId: $$PARAM_MSG_ID, ReplyIDs: $$PARAM_REPLY_IDS, addressID: $$PARAM_ADDRESS_ID,
            districtID: $$PARAM_DISTRICT_ID, postalCode: $$PARAM_POSTAL_CODE, latlon: $$PARAM_LAT_LON
          ) {
            list {
             id
              type
              attributes
              fallback {
                message
              	html
              }
              isActual
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: ChatAttachmentParam): ChatAttachmentResponse {
        return repository.request(graphqlQuery(), params)
    }

}