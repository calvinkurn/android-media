package com.tokopedia.inbox.universalinbox.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inbox.universalinbox.data.UniversalInboxLocalRepository
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWrapperResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UniversalInboxGetInboxMenuAndWidgetMetaUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val localRepository: UniversalInboxLocalRepository,
    private val dispatcher: CoroutineDispatchers
) {
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
        return localRepository.getInboxMenuCache()
            .catch {
                Timber.d(it)
                emit(UniversalInboxWrapperResponse())
            }
            .flowOn(dispatcher.io)
            .distinctUntilChanged()
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
