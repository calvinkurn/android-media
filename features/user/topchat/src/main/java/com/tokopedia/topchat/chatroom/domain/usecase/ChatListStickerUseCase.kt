package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import javax.inject.Inject

open class ChatListStickerUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<ChatListStickerUseCase.Param, StickerResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query chatBundleSticker($$PARAM_GROUP_UUID: String!, $$PARAM_LIMIT: Int!) {
          chatBundleSticker(groupUUID:$$PARAM_GROUP_UUID, limit:$$PARAM_LIMIT){
            list {
              imageUrl
              intention
              stickerUUID
              groupUUID
            }
            hasNext
            maxUUID
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Param): StickerResponse {
        return repository.request(graphqlQuery(), params)
    }

    data class Param (
        @SerializedName(PARAM_GROUP_UUID)
        var stickerGroupUID: String,

        @SerializedName(PARAM_LIMIT)
        var limit: Int = DEFAULT_PARAM_LIMIT
    ): GqlParam

    companion object {
        const val PARAM_GROUP_UUID = "groupUUID"
        const val PARAM_LIMIT = "limit"
        private const val DEFAULT_PARAM_LIMIT = 16
    }
}