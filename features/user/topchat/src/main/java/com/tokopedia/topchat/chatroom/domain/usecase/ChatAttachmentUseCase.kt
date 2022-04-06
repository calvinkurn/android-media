package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import javax.inject.Inject

open class ChatAttachmentUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<ChatAttachmentUseCase.Param, ChatAttachmentResponse>(dispatcher.io) {

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

    override suspend fun execute(params: Param): ChatAttachmentResponse {
        return repository.request(graphqlQuery(), params)
    }

    data class Param (
        @SerializedName(PARAM_MSG_ID)
        var msgId: Long = 0,

        @SerializedName(PARAM_REPLY_IDS)
        var replyIDs: String = "",

        @SerializedName(PARAM_ADDRESS_ID)
        var addressId: Long = 0,

        @SerializedName(PARAM_DISTRICT_ID)
        var districtId: Long = 0,

        @SerializedName(PARAM_POSTAL_CODE)
        var postalCode: String = "",

        @SerializedName(PARAM_LAT_LON)
        var latlon: String = ""
    ): GqlParam

    companion object {
        const val PARAM_MSG_ID = "msgId"
        const val PARAM_REPLY_IDS = "ReplyIDs"
        const val PARAM_ADDRESS_ID = "addressID"
        const val PARAM_DISTRICT_ID = "districtID"
        const val PARAM_POSTAL_CODE = "postalCode"
        const val PARAM_LAT_LON = "latlon"
    }
}