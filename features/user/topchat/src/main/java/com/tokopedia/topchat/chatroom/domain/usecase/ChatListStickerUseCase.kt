package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.common.network.TopchatCacheManager
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class ChatListStickerUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<StickerResponse>,
        private val cacheManager: TopchatCacheManager,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    private val cacheKey = ChatListStickerUseCase::class.java.simpleName
    private val paramGroupUID = "groupUUID"
    private val paramLimit = "limit"
    private val defaultParamLimit = 16

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun loadSticker(
            stickerUID: String,
            needUpdate: Boolean,
            onLoading: (List<Sticker>) -> Unit,
            onSuccess: (List<Sticker>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.io,
                {
                    val params = generateParams(stickerUID)
                    val isPreviousRequestSuccess = getPreviousRequestState(stickerUID)
                    val cache = getCacheStickerGroup(stickerUID)?.also {
                        withContext(dispatchers.main) {
                            if (!needUpdate && isPreviousRequestSuccess) {
                                onSuccess(it.chatBundleSticker.list)
                            } else {
                                onLoading(it.chatBundleSticker.list)
                            }
                        }
                    }
                    if (cache != null && !needUpdate && isPreviousRequestSuccess) return@launchCatchError
                    val response = gqlUseCase.apply {
                        setTypeClass(StickerResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    saveToCache(stickerUID, response)
                    saveSuccessRequestState(stickerUID)
                    withContext(dispatchers.main) {
                        onSuccess(response.chatBundleSticker.list)
                    }
                },
                { exception ->
                    withContext(dispatchers.main) {
                        saveFailRequestState(stickerUID)
                        onError(exception)
                    }
                }
        )
    }

    private fun getPreviousRequestState(stickerUID: String): Boolean {
        try {
            val stateCacheKey = generateStateCacheKey(stickerUID)
            return cacheManager.getPreviousState(stateCacheKey)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun saveSuccessRequestState(stickerUID: String) {
        saveRequestState(stickerUID, true)
    }

    private fun saveFailRequestState(stickerUID: String) {
        saveRequestState(stickerUID, false)
    }

    private fun saveRequestState(stickerUID: String, isSuccess: Boolean) {
        val stateCacheKey = generateStateCacheKey(stickerUID)
        cacheManager.saveState(stateCacheKey, isSuccess)
    }

    private fun generateStateCacheKey(stickerUID: String): String {
        return "state - $stickerUID"
    }

    fun safeCancel() {
        if (coroutineContext.isActive) {
            cancel()
        }
    }

    private fun saveToCache(stickerUID: String, result: StickerResponse) {
        val key = generateCacheKey(stickerUID)
        cacheManager.saveCache(key, result)
    }

    private fun getCacheStickerGroup(stickerUID: String): StickerResponse? {
        try {
            val key = generateCacheKey(stickerUID)
            return cacheManager.loadCache(key, StickerResponse::class.java)
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

    private fun generateCacheKey(stickerUID: String): String {
        return "$cacheKey - $stickerUID"
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