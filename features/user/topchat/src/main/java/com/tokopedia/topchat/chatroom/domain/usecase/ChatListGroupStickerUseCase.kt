package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.common.network.TopchatCacheManager
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class ChatListGroupStickerUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatListGroupStickerResponse>,
        private val cacheManager: TopchatCacheManager,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    private val cacheKey = ChatListGroupStickerUseCase::class.java.simpleName
    private val paramStickerType = "stickerType"
    private val stickerTypeBuyer = 0
    private val stickerTypeSeller = 1

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun safeCancel() {
        if (coroutineContext.isActive) {
            cancel()
        }
    }

    fun getStickerGroup(
            isSeller: Boolean,
            onLoading: (ChatListGroupStickerResponse) -> Unit,
            onSuccess: (ChatListGroupStickerResponse, List<StickerGroup>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.io,
                {
                    val params = generateParams(isSeller)
                    val cache = getCacheStickerGroup(isSeller)?.also {
                        withContext(dispatchers.main) {
                            onLoading(it)
                        }
                    }
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatListGroupStickerResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val hasDifferentSize = response.hasDifferentGroupSize(cache)
                    val needToUpdateCache = findNeedToUpdateCache(cache, response)
                    if (hasDifferentSize || needToUpdateCache.isNotEmpty()) {
                        saveToCache(response, isSeller)
                    }
                    withContext(dispatchers.main) {
                        onSuccess(response, needToUpdateCache)
                    }
                },
                { exception ->
                    withContext(dispatchers.main) {
                        onError(exception)
                    }
                }
        )
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

    private fun generateCacheKey(isSeller: Boolean): String {
        return "$cacheKey - $isSeller"
    }

    private fun saveToCache(result: ChatListGroupStickerResponse, isSeller: Boolean) {
        val key = generateCacheKey(isSeller)
        cacheManager.saveCache(key, result)
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