package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.common.network.TopchatCacheManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class GetChatListGroupStickerUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val cacheManager: TopchatCacheManager,
    dispatcher: CoroutineDispatchers,
): FlowUseCase<Boolean, Pair<ChatListGroupStickerResponse, List<StickerGroup>>>(dispatcher.io) {

    private val cacheKey = GetChatListGroupStickerUseCase::class.java.simpleName

    override fun graphqlQuery(): String = """
        query chatListGroupSticker($$PARAM_STICKER_TYPE: Int!){
          chatListGroupSticker(stickerType: $$PARAM_STICKER_TYPE) {
            list{
              groupUUID
              lastUpdate
              thumbnail
              title
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(
        isSeller: Boolean
    ): Flow<Pair<ChatListGroupStickerResponse, List<StickerGroup>>> = flow {
        val cache = getCacheStickerGroup(isSeller)
        if (cache != null) {
            emit(Pair(cache, emptyList()))
        }
        val param = generateParams(isSeller)
        val response = repository.request<Map<String, Any>, ChatListGroupStickerResponse>(
            graphqlQuery(), param)
        val hasDifferentSize = response.hasDifferentGroupSize(cache)
        val needToUpdateCache = findNeedToUpdateCache(cache, response)
        if (hasDifferentSize || needToUpdateCache.isNotEmpty()) {
            emit(Pair(response, needToUpdateCache))
            saveToCache(response, isSeller)
        }
    }

    private fun generateParams(isSeller: Boolean): Map<String, Any> {
        val stickerType = if (isSeller) STICKER_TYPE_SELLER else STICKER_TYPE_BUYER
        return mapOf(
            PARAM_STICKER_TYPE to stickerType
        )
    }

    private fun getCacheStickerGroup(isSeller: Boolean): ChatListGroupStickerResponse? {
        try {
            val key = generateCacheKey(isSeller)
            return cacheManager.loadCache(key, ChatListGroupStickerResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun generateCacheKey(isSeller: Boolean): String {
        return "$cacheKey - $isSeller"
    }

    private fun findNeedToUpdateCache(
        cache: ChatListGroupStickerResponse?,
        response: ChatListGroupStickerResponse
    ): List<StickerGroup> {
        if (cache == null) return emptyList()
        val cacheMap = ArrayMap<String, StickerGroup>()
        val invalidCache = ArrayList<StickerGroup>()
        for (stickerGroup in cache.stickerGroups) {
            cacheMap[stickerGroup.groupUUID] = stickerGroup
        }
        for (stickerGroup in response.stickerGroups) {
            val cached = cacheMap[stickerGroup.groupUUID]
            if (cached == null) {
                invalidCache.add(stickerGroup)
                continue
            }
            if (cached.lastUpdate != stickerGroup.lastUpdate) {
                invalidCache.add(stickerGroup)
            }
        }
        return invalidCache
    }

    private fun saveToCache(result: ChatListGroupStickerResponse, isSeller: Boolean) {
        val key = generateCacheKey(isSeller)
        cacheManager.saveCache(key, result)
    }

    companion object {
        private const val PARAM_STICKER_TYPE = "stickerType"
        private const val STICKER_TYPE_BUYER = 0
        private const val STICKER_TYPE_SELLER = 1
    }
}