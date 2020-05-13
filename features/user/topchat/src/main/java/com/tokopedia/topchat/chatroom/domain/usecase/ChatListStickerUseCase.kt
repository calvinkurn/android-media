package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.common.network.TopchatCacheManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ChatListStickerUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<StickerResponse>,
        private val cacheManager: TopchatCacheManager,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    private val cacheKey = ChatListStickerUseCase::class.java.simpleName
    private val paramGroupUID = "groupUUID"
    private val paramLimit = "limit"
    private val defaultParamLimit = 16

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun loadSticker(
            stickerUID: String,
            onLoading: (List<Sticker>) -> Unit,
            onSuccess: (List<Sticker>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.IO,
                {
                    val params = generateParams(stickerUID)
                    getCacheStickerGroup(stickerUID)?.also {
                        withContext(dispatchers.Main) {
                            onLoading(it.chatBundleSticker.list)
                        }
                    }
                    val response = gqlUseCase.apply {
                        setTypeClass(StickerResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    saveToCache(stickerUID, response)
                    withContext(dispatchers.Main) {
                        onSuccess(response.chatBundleSticker.list)
                    }
                },
                { exception ->
                    withContext(dispatchers.Main) {
                        onError(exception)
                    }
                }
        )
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