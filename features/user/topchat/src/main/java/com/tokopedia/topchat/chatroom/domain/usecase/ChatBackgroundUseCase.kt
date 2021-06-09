package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.pojo.background.ChatBackgroundResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.common.network.TopchatCacheManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class ChatBackgroundUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatBackgroundResponse>,
        private val cacheManager: TopchatCacheManager,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun getBackground(
            onLoadFromCache: (String) -> Unit,
            onSuccess: (String, Boolean) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.io,
                {
                    val cacheUrl = getCacheUrl()?.also {
                        withContext(dispatchers.main) {
                            onLoadFromCache(it)
                        }
                    }
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatBackgroundResponse::class.java)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val responseImageUrl = response.chatBackground.urlImage
                    withContext(dispatchers.main) {
                        if (responseImageUrl != cacheUrl) {
                            onSuccess(responseImageUrl, true)
                            cacheManager.saveCache(cacheKey, responseImageUrl)
                        } else {
                            onSuccess(responseImageUrl, false)
                        }
                    }
                },
                {
                    withContext(dispatchers.main) {
                        onError(it)
                    }
                }
        )
    }

    private fun getCacheUrl(): String? {
        try {
            return cacheManager.loadCache(cacheKey, String::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        private val cacheKey = "cache_key_chat_background_url"
        private val query = """
            query chatBackground{
              chatBackground{
                urlImage
                urlImageDarkMode
              }
            }
        """.trimIndent()
    }
}