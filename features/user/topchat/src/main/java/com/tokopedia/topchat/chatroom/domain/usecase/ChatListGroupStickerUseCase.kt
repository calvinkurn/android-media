package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.common.network.TopchatCacheManager
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ChatListGroupStickerUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatListGroupStickerResponse>,
        private val cacheManager: TopchatCacheManager,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    private val cacheKey = ChatListGroupStickerUseCase::class.java.simpleName
    private val paramStickerType = "stickerType"
    private val stickerTypeBuyer = 0
    private val stickerTypeSeller = 1

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun safeCancel() {
        if (coroutineContext.isActive) {
            cancel()
        }
    }

    fun getStickerGroup(
            isSeller: Boolean,
            onLoading: (ChatListGroupStickerResponse) -> Unit,
            onSuccess: (ChatListGroupStickerResponse, isExpired: Boolean) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.IO,
                {
                    val params = generateParams(isSeller)
                    val cache = getCacheStickerGroup(isSeller)?.also {
                        withContext(dispatchers.Main) {
                            onLoading(it)
                        }
                    }
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatListGroupStickerResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val isExpired = response.hasExpiredCache(cache)
                    if (isExpired) {
                        saveToCache(response, isSeller)
                    }
                    withContext(dispatchers.Main) {
                        onSuccess(response, isExpired)
                    }
                },
                { exception ->
                    withContext(dispatchers.Main) {
                        onError(exception)
                    }
                }
        )
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