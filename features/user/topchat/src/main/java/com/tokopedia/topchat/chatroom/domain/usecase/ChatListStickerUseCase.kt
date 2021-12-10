package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.param.StickerParam
import com.tokopedia.topchat.chatroom.domain.pojo.param.StickerParam.Companion.PARAM_GROUP_UUID
import com.tokopedia.topchat.chatroom.domain.pojo.param.StickerParam.Companion.PARAM_LIMIT
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import javax.inject.Inject

open class ChatListStickerUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<StickerParam, StickerResponse>(dispatcher.io) {

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

    override suspend fun execute(params: StickerParam): StickerResponse {
        return repository.request(graphqlQuery(), params)
    }
}