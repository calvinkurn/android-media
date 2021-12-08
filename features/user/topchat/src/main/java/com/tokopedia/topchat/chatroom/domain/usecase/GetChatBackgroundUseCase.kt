package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.background.ChatBackgroundResponse
import com.tokopedia.topchat.common.network.TopchatCacheManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class GetChatBackgroundUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val cacheManager: TopchatCacheManager,
    dispatcher: CoroutineDispatchers,
): FlowUseCase<Unit, String>(dispatcher.io) {

    override fun graphqlQuery(): String = """
            query chatBackground{
              chatBackground{
                urlImage
                urlImageDarkMode
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Unit): Flow<String> = flow {
        val cacheResult = getCacheUrl()
        if (cacheResult != null) {
            emit(cacheResult)
        }
        val response = repository.request<Unit, ChatBackgroundResponse>(graphqlQuery(), params)
        val responseImageUrl = response.chatBackground.urlImage
        if (responseImageUrl != cacheResult) {
            emit(responseImageUrl)
            cacheManager.saveCache(CHAT_BACKGROUND_CACHE_KEY, responseImageUrl)
        }
    }

    private fun getCacheUrl(): String? {
        try {
            return cacheManager.loadCache(CHAT_BACKGROUND_CACHE_KEY, String::class.java)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        private const val CHAT_BACKGROUND_CACHE_KEY = "cache_key_chat_background_url"
    }

}