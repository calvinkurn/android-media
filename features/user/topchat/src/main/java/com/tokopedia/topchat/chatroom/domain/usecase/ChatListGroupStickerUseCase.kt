package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.common.network.TopchatCacheManager
import javax.inject.Inject

class ChatListGroupStickerUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatListGroupStickerResponse>,
        private val cacheManager: TopchatCacheManager
) {

    private val cacheKey = ChatListGroupStickerUseCase::class.java.simpleName
    private val paramStickerType = "stickerType"
    private val stickerTypeBuyer = 0
    private val stickerTypeSeller = 1

    fun getStickerGroup(
            isSeller: Boolean,
            onLoading: (ChatListGroupStickerResponse) -> Unit,
            onSuccess: (ChatListGroupStickerResponse, isExpired: Boolean) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        val params = generateParams(isSeller)
        val cache = getCacheStickerGroup()?.also {
            onLoading(it)
        }
        gqlUseCase.apply {
            setTypeClass(ChatListGroupStickerResponse::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
            execute({ result ->
                val isExpired = cache?.isExpired(result) ?: true
                saveToCache(result, isExpired)
                onSuccess(result, isExpired)
            }, { error ->
                onError(error)
            })
        }
    }

    private fun saveToCache(result: ChatListGroupStickerResponse, isExpired: Boolean) {
        if (isExpired) {
            cacheManager.saveCache(cacheKey, result)
        }
    }

    private fun getCacheStickerGroup(): ChatListGroupStickerResponse? {
        try {
            return cacheManager.loadCache(cacheKey, ChatListGroupStickerResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun generateParams(isSeller: Boolean): Map<String, Any> {
        val stickerType = if (isSeller) stickerTypeSeller else stickerTypeBuyer
        return mapOf(
                paramStickerType to stickerType
        )
    }

    private val query = """
        query chatListGroupSticker($$paramStickerType: Int!){
          chatListGroupSticker(stickerType: $$paramStickerType) {
            list{
              groupUUID
              lastUpdate
              thumbnail
              title
            }
          }
        }
    """.trimIndent()
}