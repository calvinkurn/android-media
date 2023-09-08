package com.tokopedia.inbox.universalinbox.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inbox.universalinbox.data.UniversalInboxLocalRepository
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWrapperResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UniversalInboxGetInboxMenuAndWidgetMetaUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val localRepository: UniversalInboxLocalRepository,
    private val dispatcher: CoroutineDispatchers
) {

    private val gson: Gson by lazy {
        Gson()
    }
    private fun graphqlQuery(): String = """
        query GetChatInboxWidgetMeta {
          chatInboxWidgetMeta {
            metadata {
              icon
              title
              subtext
              androidApplink
              type
              isDynamic
            }
            inboxMenu{
              icon
              title
              appLink
              label{
                color
                text
              }
              type
            }
          }
        }
    """.trimIndent()

    suspend fun observe(): Flow<UniversalInboxWrapperResponse?> {
        return localRepository.getInboxMenuCacheObserver()
            .map {
                return@map try {
                    val resultObj = gson.fromJson(it, UniversalInboxWrapperResponse::class.java)
                    resultObj
                } catch (throwable: Throwable) {
                    Timber.d(throwable)
                    null
                }
            }
            .catch {
                Timber.d(it)
                emit(null)
            }
            .flowOn(dispatcher.io)
    }

    suspend fun fetchInboxMenuAndWidgetMeta(params: Unit) {
        withContext(dispatcher.io) {
            val result = repository.request<Unit, UniversalInboxWrapperResponse>(
                graphqlQuery(),
                params
            )
            updateCache(result)
        }
    }

    suspend fun updateCache(
        value: UniversalInboxWrapperResponse
    ) {
        // try-catch block is used to prevent obstructing the flow
        // in case of a failure to save to the cache
        try {
            localRepository.setInboxMenuCache(value)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }
}
