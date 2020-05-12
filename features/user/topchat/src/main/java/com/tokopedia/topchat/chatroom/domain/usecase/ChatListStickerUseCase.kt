package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.common.network.TopchatCacheManager
import javax.inject.Inject

class ChatListStickerUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<StickerResponse>,
        private val cacheManager: TopchatCacheManager
) {

    private val cacheKey = ChatListGroupStickerUseCase::class.java.simpleName
    private val paramGroupUID = "groupUUID"
    private val paramLimit = "limit"
    private val defaultParamLimit = 16

    fun loadSticker(
            stickerUID: String,
            onLoading: (List<Sticker>) -> Unit,
            onSuccess: (List<Sticker>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        val params = generateParams(stickerUID)
        getCacheStickerGroup()?.also {
            onLoading(it.chatBundleSticker.list)
        }
        gqlUseCase.apply {
            setTypeClass(StickerResponse::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
            execute({ result ->
                saveToCache(result)
                onSuccess(result.chatBundleSticker.list)
            }, { error ->
                onError(error)
            })
        }
    }

    private fun saveToCache(result: StickerResponse) {
        cacheManager.saveCache(cacheKey, result)
    }

    private fun getCacheStickerGroup(): StickerResponse? {
        try {
            return cacheManager.loadCache(cacheKey, StickerResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun generateParams(stickerUID: String): Map<String, Any> {
        return mapOf(
                paramGroupUID to stickerUID,
                paramLimit to defaultParamLimit
        )
    }

    private val query = """
        query chatBundleSticker($$paramGroupUID: String!, $$paramLimit: Int!) {
          chatBundleSticker(groupUUID:$$paramGroupUID, limit:$$paramLimit){
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

}