package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokochat.domain.response.background.TokoChatBackgroundResponse
import com.tokopedia.tokochat.common.util.TokoChatCacheManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTokoChatBackgroundUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val cacheManager: TokoChatCacheManager,
    dispatcher: CoroutineDispatchers
) : FlowUseCase<Unit, String>(dispatcher.io) {

    override fun graphqlQuery(): String = """
            query tokoChatBackground{
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
        val response = repository.request<Unit, TokoChatBackgroundResponse>(graphqlQuery(), params)
        val responseImageUrl = response.tokoChatBackground.urlImage
        if (responseImageUrl != cacheResult) {
            emit(responseImageUrl)
            cacheManager.saveCache(TOKO_CHAT_BACKGROUND_CACHE_KEY, responseImageUrl)
        }
    }

    private fun getCacheUrl(): String? {
        try {
            return cacheManager.loadCache(TOKO_CHAT_BACKGROUND_CACHE_KEY, String::class.java)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        private const val TOKO_CHAT_BACKGROUND_CACHE_KEY = "tokochat_key_chat_background_url"
    }
}
